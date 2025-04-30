package pl.dminior8.cart_service.application.cart.command.RemoveProductFromCart;


import lombok.Builder;
import pl.dminior8.cart_service.application.cart.command.CartCommand;

import java.util.UUID;


@Builder
public record RemoveProductFromCartCommand(UUID userId, UUID productId, int quantity) implements CartCommand {}


