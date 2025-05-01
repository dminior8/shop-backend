package pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.application.shoppingCart.command.createCart.CreateCartCommand;
import pl.dminior8.cart_service.application.shoppingCart.command.createCart.CreateCartCommandHandler;
import pl.dminior8.cart_service.domain.event.ProductReservedEvent;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import pl.dminior8.cart_service.infrastructure.messaging.DomainEventPublisher;
import pl.dminior8.cart_service.infrastructure.redis.CartActivityService;
import pl.dminior8.cart_service.infrastructure.redis.ProductLockService;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.common.exceptions.product.ProductLockedException;
import pl.dminior8.common.exceptions.product.ProductNotAvailableException;


@Component
@Slf4j
public class AddProductToCartCommandHandler {
    private final CartRepository cartRepo;
    private final ExternalProductServiceClient productClient;
    private final DomainEventPublisher eventPublisher;
    private final ProductLockService lockService;
    private final CartActivityService activityService;
    private final CreateCartCommandHandler createCartCommandHandler;

    public AddProductToCartCommandHandler(CartRepository cartRepo,
                                          ExternalProductServiceClient productClient,
                                          DomainEventPublisher eventPublisher,
                                          ProductLockService lockService,
                                          CartActivityService activityService,
                                          CreateCartCommandHandler createCartCommandHandler) {
        this.cartRepo = cartRepo;
        this.productClient = productClient;
        this.eventPublisher = eventPublisher;
        this.lockService = lockService;
        this.activityService = activityService;
        this.createCartCommandHandler = createCartCommandHandler;
    }

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        // 1. Pobierz lub utwórz agregat Cart
        Cart cart = cartRepo.findByUserId(cmd.userId())
                .orElseGet(() -> createCartCommandHandler.handle(new CreateCartCommand(cmd.userId())));

        // 2. Weryfikacja stanu produktu i blokada produktu
        ProductDto product = productClient.getProductById(String.valueOf(cmd.productId()));
        if(product.getAvailableQuantity() >= cmd.quantity()){
            boolean locked = lockService.tryLockProduct(cmd.productId(), cart.getId());
            if (!locked) {
                throw new ProductLockedException(cmd.productId());
            }
        } else {
            throw new ProductNotAvailableException(cmd.productId(), cmd.quantity(), product.getAvailableQuantity());
        }

        // 3. Logika biznesowa w agregacie
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
