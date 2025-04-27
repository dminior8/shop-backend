package pl.dminior8.cart_service.domain.event;


import java.time.Instant;
import java.util.UUID;

public record CartCheckedOut(UUID cartId, UUID userId, Instant occurredAt) {
    public CartCheckedOut(UUID cartId, UUID userId) {
        this(cartId, userId, Instant.now());
    }
}

