package pl.dminior8.cart_service.application.cart.command.RemoveProductFromCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.application.cart.command.AddProductToCart.AddProductToCartCommand;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class RemoveProductFromCartCommandHandler {

    private final CartRepository cartRepo;
    private final DomainEventPublisher eventPublisher;

    public RemoveProductFromCartCommandHandler(CartRepository cartRepo,
                                               DomainEventPublisher eventPublisher) {
        this.cartRepo = cartRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        // 1. Załaduj agregat Cart
        Cart cart = cartRepo.findByUserId(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        // 2. Logika usuwania w agregacie
        cart.removeProduct(cmd.productId(), cmd.quantity());

        // 3. Zapis agregatu
        cartRepo.save(cart);

        // 4. Publikacja zdarzeń (ProductRemovedEvent)
        for (Object ev : cart.pullDomainEvents()) {
            eventPublisher.publish(ev);
        }
    }
}
