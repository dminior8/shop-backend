package pl.dminior8.order_service.infrastructure.messaging.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dminior8.common.event.CartCheckedOutEvent;
import pl.dminior8.order_service.domain.service.OrderService;

@Slf4j
@Component
public class CartEventListener {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public CartEventListener(ObjectMapper objectMapper, OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @RabbitListener(queues = "cart.checkedout.queue")
    public void handleCartCheckedOut(String message) {
        try {
            CartCheckedOutEvent event = objectMapper.readValue(message, CartCheckedOutEvent.class);
            orderService.createOrder(event.userId(), event.cartId());
        } catch (Exception e) {
            log.error("Error while processing CartCheckedOutEvent: " + e.getMessage());
        }
    }
}

