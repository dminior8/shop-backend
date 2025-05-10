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
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.common.exceptions.product.ProductNotAvailableException;


@Component
@Slf4j
public class AddProductToCartCommandHandler {
    private final CartRepository cartRepo;
    private final ExternalProductServiceClient productClient;
    private final DomainEventPublisher eventPublisher;
    private final CartActivityService activityService;
    private final CreateCartCommandHandler createCartCommandHandler;

    public AddProductToCartCommandHandler(CartRepository cartRepo,
                                          ExternalProductServiceClient productClient,
                                          DomainEventPublisher eventPublisher,
                                          CartActivityService activityService,
                                          CreateCartCommandHandler createCartCommandHandler) {
        this.cartRepo = cartRepo;
        this.productClient = productClient;
        this.eventPublisher = eventPublisher;
        this.activityService = activityService;
        this.createCartCommandHandler = createCartCommandHandler;
    }

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        // 1. Pobierz lub utwórz agregat Cart
        Cart cart = cartRepo.findByUserId(cmd.userId())
                .orElseGet(() -> createCartCommandHandler.handle(new CreateCartCommand(cmd.userId())));

        // 2. Weryfikacja stanu produktu
        ProductDto product = productClient.getProductById(cmd.productId());

        if(product.getAvailableQuantity() < cmd.quantity()){
            throw new ProductNotAvailableException(cmd.productId(), cmd.quantity(), product.getAvailableQuantity());
        }

        // 3. Logika biznesowa w agregacie
        cart.addProduct(cmd.productId(), cmd.quantity(), product.getPrice());

        // 4. Zapis agregatu i odświeżenie TTL koszyka
        cartRepo.save(cart);

        // 5. Rezerwacja produktu
        productClient.reserveProduct(
                cmd.productId(),
                cmd.userId(),
                cart.getId(),
                cmd.quantity()
        );

        // 6. Odświeżenie ważności koszyka
        activityService.refreshCartTtl(cart.getId());

        // 7. Publikacja zdarzeń domenowych
        for (Object ev : cart.pullDomainEvents()) {
            if (ev instanceof ProductReservedEvent) {
                eventPublisher.publish(ev);
            }
        }
    }
}
