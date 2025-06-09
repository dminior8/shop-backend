package pl.dminior8.cart_service.application.shoppingCart.command.removeProductFromCart;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.redis.CartActivityService;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.common.event.ProductRemovedEvent;

import static pl.dminior8.cart_service.infrastructure.messaging.publishers.RabbitMqDomainEventPublisher.PRODUCT_REMOVED_ROUTING_KEY;

@Component
public class RemoveProductFromCartCommandHandler {
    @Value("${messaging.exchange.cart-events}")
    private String cartEventsExchange;

    private final CartRepository cartRepository;
    private final CartActivityService activityService;
    private final RabbitTemplate rabbitTemplate;

    public RemoveProductFromCartCommandHandler(CartRepository cartRepository,
                                               CartActivityService activityService,
                                               RabbitTemplate rabbitTemplate) {
        this.cartRepository = cartRepository;
        this.activityService = activityService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public void handle(RemoveProductFromCartCommand cmd) {
        // 1. Załaduj agregat Cart
        Cart cart = cartRepository.findByUserId(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        try {
            // 2. Logika usuwania w agregacie
            cart.removeProduct(cmd.productId(), cmd.quantity());

            // 3. Publikacja zdarzenia domenowego i zapis agregatu
            for (Object ev : cart.pullDomainEvents()) {
                if (ev instanceof ProductRemovedEvent) {
                    rabbitTemplate.convertSendAndReceive(
                            cartEventsExchange,
                            PRODUCT_REMOVED_ROUTING_KEY,
                            ev
                    );
                    cartRepository.save(cart);
                    activityService.expireCartTtl(cart.getId());
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to remove product from cart: " + e.getMessage());
        }
    }
}

