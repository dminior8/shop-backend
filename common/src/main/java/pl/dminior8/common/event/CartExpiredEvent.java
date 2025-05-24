package pl.dminior8.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CartExpiredEvent(UUID cartId, UUID userId) {
    @JsonCreator
    public CartExpiredEvent(@JsonProperty("cartId") UUID cartId, @JsonProperty("userId") UUID userId) {
        this.cartId = cartId;
        this.userId = userId;
    }
}
