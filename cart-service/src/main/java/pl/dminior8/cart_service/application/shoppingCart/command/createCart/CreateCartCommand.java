package pl.dminior8.cart_service.application.shoppingCart.command.createCart;

import lombok.Builder;
import pl.dminior8.cart_service.application.shoppingCart.command.CartCommand;

import java.util.UUID;


@Builder
public record CreateCartCommand(UUID userId) implements CartCommand {}

