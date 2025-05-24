package pl.dminior8.common.event;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record CartCreatedEvent(UUID cartId, UUID userId, Instant occurredAt) {
    @JsonCreator
    public CartCreatedEvent(@JsonProperty("cartId") UUID cartId, @JsonProperty("userId") UUID userId) {
        this(cartId, userId, Instant.now());
    }
}

