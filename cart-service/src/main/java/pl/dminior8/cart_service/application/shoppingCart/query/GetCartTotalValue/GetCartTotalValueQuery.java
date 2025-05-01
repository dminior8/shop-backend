package pl.dminior8.cart_service.application.shoppingCart.query.GetCartTotalValue;


import java.util.Objects;
import java.util.UUID;

/**
 * Zapytanie o łączną wartość koszyka danego użytkownika.
 */
public record GetCartTotalValueQuery(UUID userId) {
    public GetCartTotalValueQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.toString().isEmpty()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
    }
}

