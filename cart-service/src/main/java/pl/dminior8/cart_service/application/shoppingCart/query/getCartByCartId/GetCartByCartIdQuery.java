package pl.dminior8.cart_service.application.shoppingCart.query.getCartByCartId;

import java.util.Objects;
import java.util.UUID;


public record GetCartByCartIdQuery(UUID userId, UUID cartId) {
    public GetCartByCartIdQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.toString().isEmpty()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
        Objects.requireNonNull(cartId, "cartId must not be null");
        if (cartId.toString().isEmpty()) {
            throw new IllegalArgumentException("cartId must not be blank");
        }
    }
}