package pl.dminior8.cart_service.application.shoppingCart.command.createCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.common.event.CartCreatedEvent;
import pl.dminior8.cart_service.infrastructure.messaging.publishers.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CreateCartCommandHandler {

    private final CartRepository cartRepository;
    private final DomainEventPublisher eventPublisher;

    public CreateCartCommandHandler(CartRepository cartRepository,
                                    DomainEventPublisher eventPublisher) {
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Cart handle(CreateCartCommand cmd) {
        AtomicBoolean isNewCart = new AtomicBoolean(false);

        // 1. Utwórz agregat Cart
        Cart cart = cartRepository.findByUserId(cmd.userId())
                .orElseGet(() -> {
                    Cart newCart = Cart.create(cmd.userId());
                    isNewCart.set(true);
                    return newCart;
                });

        // 2. Zapis agregatu
        cartRepository.save(cart);

        // 3. Zapis zdarzenia domenowego
        if(isNewCart.get()) {
            cart.getDomainEvents().add(new CartCreatedEvent(cart.getId(), cart.getUserId()));
        }

        // 4. Publikacja zdarzeń (np. CartCreatedEvent)
        for (Object ev : cart.pullDomainEvents()) {
            eventPublisher.publish(ev);
        }

        return cart;
    }
}
