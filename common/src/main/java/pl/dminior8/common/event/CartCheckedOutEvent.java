package pl.dminior8.common.event;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public record CartCheckedOutEvent(
        UUID cartId,
        UUID userId,
        Instant occurredAt
) {
    @JsonCreator
    public CartCheckedOutEvent(
            @JsonProperty("cartId") UUID cartId,
            @JsonProperty("userId") UUID userId,
            @JsonProperty("occurredAt") Instant occurredAt
    ) {
        this.cartId = cartId;
        this.userId = userId;
        this.occurredAt = occurredAt;
    }

    public CartCheckedOutEvent(UUID cartId, UUID userId) {
        this(cartId, userId, Instant.now());
    }
}

