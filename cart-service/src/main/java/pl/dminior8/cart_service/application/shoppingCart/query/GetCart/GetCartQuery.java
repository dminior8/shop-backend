package pl.dminior8.cart_service.application.shoppingCart.query.GetCart;

import java.util.Objects;
import java.util.UUID;


public record GetCartQuery(UUID userId) {
    public GetCartQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.toString().isEmpty()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
    }
}

