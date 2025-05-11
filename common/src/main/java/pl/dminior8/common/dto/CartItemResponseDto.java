package pl.dminior8.common.dto;


import java.util.UUID;

public record CartItemResponseDto(
        UUID productId,
        int quantity,
        double price
) {
}

