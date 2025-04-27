package pl.dminior8.cart_service.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public record CartCommand(
        UUID userId,
        UUID productId,
        int quantity
) {

}
