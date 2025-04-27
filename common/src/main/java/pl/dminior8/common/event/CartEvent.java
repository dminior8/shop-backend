package pl.dminior8.common.event;

import java.io.Serializable;

public record CartEvent(
        String userId,
        String productId,
        EType type
) implements Serializable {}
