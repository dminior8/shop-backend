package pl.dminior8.cart_service.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ProductReserved(UUID cartId, UUID productId, int quantity, Instant occurredAt) {
    public ProductReserved(UUID cartId, UUID productId, int quantity) {
        this(cartId, productId, quantity, Instant.now());
    }
}

