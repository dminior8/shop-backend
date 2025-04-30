package pl.dminior8.cart_service.application.cart.command.AddProductToCart;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.domain.event.ProductReservedEvent;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.redis.CartActivityService;
import pl.dminior8.cart_service.infrastructure.redis.ProductLockService;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.common.exceptions.product.ProductLockedException;


@Component
public class AddProductToCartCommandHandler {
    private final CartRepository cartRepo;
    private final ExternalProductServiceClient productClient;
    private final DomainEventPublisher eventPublisher;
    private final ProductLockService lockService;
    private final CartActivityService activityService;

    public AddProductToCartCommandHandler(CartRepository cartRepo,
                                          ExternalProductServiceClient productClient,
                                          DomainEventPublisher eventPublisher,
                                          ProductLockService lockService,
                                          CartActivityService activityService) {
        this.cartRepo = cartRepo;
        this.productClient = productClient;
        this.eventPublisher = eventPublisher;
        this.lockService = lockService;
        this.activityService = activityService;
    }

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        // 1. Pobierz lub utwórz agregat Cart
        Cart cart = cartRepo.findByUserId(cmd.userId())
                .orElseGet(() -> Cart.create(cmd.userId()));

        // 2. Weryfikacja stanu produktu
        productClient.getProductById(String.valueOf(cmd.productId()));

        // 3. Weryfikacja i blokada produktu
        productClient.getProductById(String.valueOf(cmd.productId()));
        boolean locked = lockService.tryLockProduct(cmd.productId(), cart.getId());
        if (!locked) {
            throw new ProductLockedException(cmd.productId());
        }

        // 4. Logika biznesowa w agregacie
        cart.addProduct(cmd.productId(), cmd.quantity());

        // 4. Zapis agregatu i odświeżenie TTL koszyka
        cartRepo.save(cart);
        activityService.refreshCartTtl(cart.getId());

        // 5. Publikacja zdarzeń domenowych
        for (Object ev : cart.pullDomainEvents()) {
            if (ev instanceof ProductReservedEvent pr) {
                eventPublisher.publish(ev);
            }
        }
    }
}
