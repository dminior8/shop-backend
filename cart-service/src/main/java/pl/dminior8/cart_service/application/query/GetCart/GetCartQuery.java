package pl.dminior8.cart_service.application.query.GetCart;

import java.util.Objects;

/**
 * Zapytanie o cały koszyk danego użytkownika.
 */
public record GetCartQuery(String userId) {
    public GetCartQuery {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
    }
}

