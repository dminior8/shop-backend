package pl.dminior8.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartDto {
    private UUID id;

    private UUID userId;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    private List<CartItemDto> items = new ArrayList<>();
}
