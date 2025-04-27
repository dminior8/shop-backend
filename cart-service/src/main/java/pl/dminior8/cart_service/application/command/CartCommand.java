package pl.dminior8.cart_service.application.command;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CartCommand(
        UUID userId,
        UUID productId,
        int quantity
) {

}
