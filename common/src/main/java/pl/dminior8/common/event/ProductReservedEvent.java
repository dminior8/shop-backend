package pl.dminior8.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record ProductReservedEvent(UUID cartItemId, UUID cartId, UUID productId, int quantity, Instant occurredAt) {
    @JsonCreator
    public ProductReservedEvent(@JsonProperty("cartItemId") UUID cartItemId, @JsonProperty("cartId") UUID cartId, @JsonProperty("productId") UUID productId, @JsonProperty("quantity") int quantity, @JsonProperty("occurredAt") Instant occurredAt) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.occurredAt = occurredAt;
    }
    public ProductReservedEvent(UUID cartItemId, UUID cartId, UUID productId, int quantity) {
        this(cartItemId, cartId, productId, quantity, Instant.now());
    }
}

