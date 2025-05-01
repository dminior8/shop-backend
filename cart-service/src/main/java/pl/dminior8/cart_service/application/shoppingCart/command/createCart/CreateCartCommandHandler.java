package pl.dminior8.cart_service.application.shoppingCart.command.createCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart.AddProductToCartCommand;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class CreateCartCommandHandler {

    private final CartRepository cartRepo;
    private final DomainEventPublisher eventPublisher;

    public CreateCartCommandHandler(CartRepository cartRepo,
                                    DomainEventPublisher eventPublisher) {
        this.cartRepo = cartRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Cart handle(CreateCartCommand cmd) {
        // 1. Utwórz agregat Cart
        Cart cart = Cart.create(cmd.userId());

        // 2. Zapis agregatu
        cartRepo.save(cart);

        // 3. Publikacja zdarzeń (np. CartCreatedEvent)
        for (Object ev : cart.pullDomainEvents()) {
            eventPublisher.publish(ev);
        }

        return cart;
    }
}
