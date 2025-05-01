package pl.dminior8.cart_service.application.shoppingCart.command.checkoutCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart.AddProductToCartCommand;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class CheckoutCartCommandHandler {

    private final CartRepository cartRepo;
    private final DomainEventPublisher eventPublisher;

    public CheckoutCartCommandHandler(CartRepository cartRepo,
                                      DomainEventPublisher eventPublisher) {
        this.cartRepo = cartRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        // 1. Załaduj agregat Cart
        Cart cart = cartRepo.findByUserId(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        // 2. Logika checkoutu w agregacie
        cart.checkout();

        // 3. Zapis agregatu
        cartRepo.save(cart);

        // 4. Publikacja wszystkich wygenerowanych zdarzeń
        for (Object ev : cart.pullDomainEvents()) {
            eventPublisher.publish(ev);
        }
    }
}