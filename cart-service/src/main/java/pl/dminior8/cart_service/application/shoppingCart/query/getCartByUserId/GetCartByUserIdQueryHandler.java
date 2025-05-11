package pl.dminior8.cart_service.application.shoppingCart.query.getCartByUserId;

import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class GetCartByUserIdQueryHandler {

    private final CartRepository cartRepository;

    public GetCartByUserIdQueryHandler(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart handle(GetCartByUserIdQuery query) {
        return cartRepository.findByUserId(query.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }
}

