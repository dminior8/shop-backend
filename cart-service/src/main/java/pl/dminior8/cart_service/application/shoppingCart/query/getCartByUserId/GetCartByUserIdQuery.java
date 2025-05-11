package pl.dminior8.cart_service.application.shoppingCart.query.getCartByUserId;

import java.util.Objects;
import java.util.UUID;


public record GetCartByUserIdQuery(UUID userId) {
    public GetCartByUserIdQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.toString().isEmpty()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
    }
}

