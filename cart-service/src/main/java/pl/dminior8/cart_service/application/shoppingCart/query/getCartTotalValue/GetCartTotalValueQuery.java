package pl.dminior8.cart_service.application.shoppingCart.query.getCartTotalValue;


import java.util.Objects;
import java.util.UUID;


public record GetCartTotalValueQuery(UUID userId) {
    public GetCartTotalValueQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.toString().isEmpty()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
    }
}

