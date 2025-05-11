package pl.dminior8.order_service.domain.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.common.dto.CartDto;
import pl.dminior8.order_service.domain.entity.Order;
import pl.dminior8.order_service.domain.entity.OrderItem;
import pl.dminior8.order_service.infrastructure.openfeign.CartServiceClient;
import pl.dminior8.order_service.infrastructure.repository.OrderRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.dminior8.order_service.domain.entity.OrderStatus.PLACED;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartServiceClient cartServiceClient;

    public OrderService(OrderRepository orderRepository, CartServiceClient cartServiceClient) {
        this.orderRepository = orderRepository;
        this.cartServiceClient = cartServiceClient;
    }

    @Transactional
    public void createOrder(UUID userId, UUID cartId) {
        CartDto cartDto = cartServiceClient.getCheckedOutCart(userId, cartId);

        double totalAmount = cartDto.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setCartId(cartId);
        order.setStatus(PLACED);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        // Ręczne zarządzanie powiązaniami
        List<OrderItem> items = cartDto.getItems().stream()
                .map(dto -> new OrderItem(orderId, dto.getProductId(), dto.getQuantity(), dto.getPrice()))
                .toList();

        order.setItems(items);
        orderRepository.save(order);

        String itemsDescription = order.getItems().stream()
                .map(i -> String.format("\n{productId=%s, quantity=%d, unitPrice=%.2f}",
                        i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.joining(", "));
        log.info("Order placed successfully \nid={}, \nuserId={}, \ncartId={}, \nstatus={}, \ntotalAmount={}, \nitems=[{}\n]",
                order.getId(),
                order.getUserId(),
                order.getCartId(),
                order.getStatus(),
                order.getTotalAmount(),
                itemsDescription);
    }
}

