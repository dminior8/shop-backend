package pl.dminior8.common.event;


import java.time.Instant;
import java.util.UUID;

public record CartCheckedOutEvent(UUID cartId, UUID userId, Instant occurredAt) {
    public CartCheckedOutEvent(UUID cartId, UUID userId) {
        this(cartId, userId, Instant.now());
    }
}

