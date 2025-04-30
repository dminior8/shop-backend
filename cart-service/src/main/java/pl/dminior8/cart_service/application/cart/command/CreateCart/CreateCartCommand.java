package pl.dminior8.cart_service.application.cart.command.CreateCart;

import lombok.Builder;
import pl.dminior8.cart_service.application.cart.command.CartCommand;

import java.util.UUID;


@Builder
public record CreateCartCommand(UUID userId) implements CartCommand {}

