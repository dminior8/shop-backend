package pl.dminior8.cart_service.domain.event;


import java.time.Instant;
import java.util.UUID;

public record ProductRemovedEvent(UUID cartId, UUID productId, int quantity, Instant occurredAt) {
    public ProductRemovedEvent(UUID cartId, UUID productId, int quantity) {
        this(cartId, productId, quantity, Instant.now());
    }
}

