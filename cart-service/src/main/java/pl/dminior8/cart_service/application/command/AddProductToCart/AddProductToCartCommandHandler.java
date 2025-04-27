package pl.dminior8.cart_service.application.command.AddProductToCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.application.command.CartCommand;
import pl.dminior8.cart_service.domain.event.ProductReserved;
import pl.dminior8.cart_service.domain.model.Cart;
import pl.dminior8.cart_service.infrastructure.external.ProductServiceClient;
import pl.dminior8.cart_service.infrastructure.external.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;


@Component
public class AddProductToCartCommandHandler {
    private final CartRepository cartRepo;
    private final ProductServiceClient productClient;
    private final DomainEventPublisher eventPublisher;

    public AddProductToCartCommandHandler(CartRepository cartRepo,
                                          ProductServiceClient productClient,
                                          DomainEventPublisher eventPublisher) {
        this.cartRepo = cartRepo;
        this.productClient = productClient;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(CartCommand cmd) {
        // 1. Pobierz lub utwórz agregat Cart
        Cart cart = cartRepo.findByUserId(String.valueOf(cmd.userId()))
                .orElseGet(() -> Cart.create(cmd.userId()));

        // 2. Weryfikacja stanu produktu
        productClient.getProductById(String.valueOf(cmd.productId()));

        // 3. Logika biznesowa w agregacie
        cart.addProduct(cmd.productId(), cmd.quantity());

        // 4. Zapis agregatu
        cartRepo.save(cart);

        // 5. Publikacja zdarzeń domenowych
        for (Object ev : cart.pullDomainEvents()) {
            if (ev instanceof ProductReserved pr) {
                eventPublisher.publish(ev);
            }
        }
    }
}
