package pl.dminior8.cart_service.application.shoppingCart.command.removeProductFromCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import pl.dminior8.cart_service.infrastructure.redis.CartActivityService;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class RemoveProductFromCartCommandHandler {

    private final CartRepository cartRepo;
    private final DomainEventPublisher eventPublisher;
    private final ExternalProductServiceClient productClient;
    private final CartActivityService activityService;

    public RemoveProductFromCartCommandHandler(CartRepository cartRepo,
                                               DomainEventPublisher eventPublisher, ExternalProductServiceClient productClient, CartActivityService activityService) {
        this.cartRepo = cartRepo;
        this.eventPublisher = eventPublisher;
        this.productClient = productClient;
        this.activityService = activityService;
    }

    @Transactional
    public void handle(RemoveProductFromCartCommand cmd) {
        // 1. Załaduj agregat Cart
        Cart cart = cartRepo.findByUserId(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        // 2. Logika usuwania w agregacie (oraz zapis agregatu)
        cart.removeProduct(cmd.productId(), cmd.quantity());
        cartRepo.save(cart);

        // 3. Zwolnienie rezerwacji częściowej w ProductService
        productClient.releaseProductPartially(
                cmd.productId(),
                cmd.userId(),
                cart.getId(),
                cmd.quantity()
        );

        // 4. Reset ważności koszyka
        activityService.expireCartTtl(cart.getId());

        // 5. Publikacja zdarzeń (ProductRemovedEvent)
        for (Object ev : cart.pullDomainEvents()) {
            eventPublisher.publish(ev);
        }
    }
}
