package pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.application.shoppingCart.command.createCart.CreateCartCommand;
import pl.dminior8.cart_service.application.shoppingCart.command.createCart.CreateCartCommandHandler;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import pl.dminior8.cart_service.infrastructure.redis.CartActivityService;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.common.event.ProductReservedEvent;
import pl.dminior8.common.exceptions.product.IncorrectProductPrice;

import java.util.Optional;
import java.util.UUID;

import static pl.dminior8.cart_service.domain.entity.CartItemStatus.RESERVED;
import static pl.dminior8.cart_service.infrastructure.messaging.publishers.RabbitMqDomainEventPublisher.PRODUCT_RESERVED_ROUTING_KEY;


@Component
@Slf4j
public class AddProductToCartCommandHandler {
    @Value("${messaging.exchange.cart-events}")
    private String cartEventsExchange;

    private final CartRepository cartRepository;
    private final ExternalProductServiceClient productClient;
    private final CartActivityService activityService;
    private final CreateCartCommandHandler createCartCommandHandler;
    private final RabbitTemplate rabbitTemplate;

    public AddProductToCartCommandHandler(CartRepository cartRepository,
                                          ExternalProductServiceClient productClient,
                                          CartActivityService activityService,
                                          CreateCartCommandHandler createCartCommandHandler, RabbitTemplate rabbitTemplate) {
        this.cartRepository = cartRepository;
        this.productClient = productClient;
        this.activityService = activityService;
        this.createCartCommandHandler = createCartCommandHandler;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        // 1. Pobierz lub utwórz agregat Cart
        Cart cart = cartRepository.findByUserId(cmd.userId())
                .orElseGet(() -> createCartCommandHandler.handle(new CreateCartCommand(cmd.userId())));

        // 2. Pobranie ceny produktu
        Optional<ProductDto> product = Optional.ofNullable(productClient.getProductById(cmd.productId()));

        // 3. Logika biznesowa w agregacie
        if (product.isEmpty()) {
            throw new NullPointerException("Product not found");
        } else if (product.get().getPrice() <= 0) {
            throw new IncorrectProductPrice(cmd.productId(), product.get().getPrice());
        } else {
            cart.addProduct(cmd.productId(), cmd.quantity(), product.get().getPrice());
        }

        // 4. Publikacja zdarzenia domenowego
        for (Object ev : cart.pullDomainEvents()) {
            if (ev instanceof ProductReservedEvent) {
                UUID cartItemId = (UUID) rabbitTemplate.convertSendAndReceive(
                        cartEventsExchange,
                        PRODUCT_RESERVED_ROUTING_KEY,
                        ev
                );
                // 5. Odświeżenie ważności koszyka
                activityService.refreshCartTtl(cart.getId());

                // 6. Zapis agregatu
                cart.getItems().stream().filter(cartItem -> cartItem.getId().equals(cartItemId)).findFirst().ifPresent(cartItem -> cartItem.setStatus(RESERVED));
                cartRepository.save(cart);
            }
        }
    }
}
