package pl.dminior8.order_service.infrastructure.messaging.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dminior8.common.event.CartCheckedOutEvent;
import pl.dminior8.order_service.domain.service.OrderService;

@Slf4j
@Component
public class CartEventListener {

    private final OrderService orderService;

    public CartEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "cart.checkedout.queue")
    public void handleCartCheckedOut(CartCheckedOutEvent event) {
        try {
            orderService.createOrder(event.userId(), event.cartId());
        } catch (Exception e) {
            log.error("Error while processing CartCheckedOutEvent: " + e.getMessage());
        }
    }
}

