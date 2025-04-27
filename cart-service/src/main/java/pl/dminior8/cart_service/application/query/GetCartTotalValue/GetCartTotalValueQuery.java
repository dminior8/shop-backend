package pl.dminior8.cart_service.application.query.GetCartTotalValue;


import java.util.Objects;

/**
 * Zapytanie o łączną wartość koszyka danego użytkownika.
 */
public record GetCartTotalValueQuery(String userId) {
    public GetCartTotalValueQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
    }
}

