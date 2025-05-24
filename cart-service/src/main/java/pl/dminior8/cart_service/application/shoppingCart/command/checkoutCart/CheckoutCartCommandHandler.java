package pl.dminior8.cart_service.application.shoppingCart.command.checkoutCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.messaging.publishers.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import pl.dminior8.cart_service.infrastructure.redis.CartActivityService;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class CheckoutCartCommandHandler {

    private final CartRepository cartRepository;
    private final DomainEventPublisher eventPublisher;
    private final ExternalProductServiceClient productClient;
    private final CartActivityService activityService;

    public CheckoutCartCommandHandler(CartRepository cartRepository,
                                      DomainEventPublisher eventPublisher,
                                      ExternalProductServiceClient productClient,
                                      CartActivityService activityService) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
        this.productClient = productClient;
        this.activityService = activityService;
    }

    @Transactional
    public void handle(CheckoutCartCommand cmd) {
        // 1. Załaduj agregat Cart
        Cart cart = cartRepository.findByUserId(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        // 2. Logika checkoutu w agregacie
        cart.checkout();

        // 3. Zapis agregatu
        cartRepository.save(cart);

        // 4. Usuwanie zarezerwowanych produtków z tabeli
        productClient.releaseByCart(cart.getId());

        // 5. Reset ważności koszyka
        activityService.expireCartTtl(cart.getId());

        // 6. Publikacja wszystkich wygenerowanych zdarzeń
        for (Object ev : cart.pullDomainEvents()) {
            eventPublisher.publish(ev);
        }
    }
}