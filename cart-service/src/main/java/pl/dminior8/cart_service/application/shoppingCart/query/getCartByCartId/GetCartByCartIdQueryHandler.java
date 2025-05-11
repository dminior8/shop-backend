package pl.dminior8.cart_service.application.shoppingCart.query.getCartByCartId;

import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class GetCartByCartIdQueryHandler {

    private final CartRepository cartRepository;

    public GetCartByCartIdQueryHandler(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart handle(GetCartByCartIdQuery query) {
        Cart cart = cartRepository.findById(query.cartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getUserId().equals(query.userId())) {
            throw new IllegalArgumentException("User ID does not match cart owner");
        }

        return cart;
    }
}