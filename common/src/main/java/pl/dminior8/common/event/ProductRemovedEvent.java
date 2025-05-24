package pl.dminior8.common.event;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record ProductRemovedEvent(UUID cartId, UUID productId, int quantity, Instant occurredAt) {
    @JsonCreator
    public ProductRemovedEvent(@JsonProperty("cartId")  UUID cartId, @JsonProperty("productId") UUID productId, @JsonProperty("quantity") int quantity) {
        this(cartId, productId, quantity, Instant.now());
    }
}

