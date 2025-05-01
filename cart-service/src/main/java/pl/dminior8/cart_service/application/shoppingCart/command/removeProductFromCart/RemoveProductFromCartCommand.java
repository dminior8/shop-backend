package pl.dminior8.cart_service.application.shoppingCart.command.removeProductFromCart;


import lombok.Builder;
import pl.dminior8.cart_service.application.shoppingCart.command.CartCommand;

import java.util.UUID;


@Builder
public record RemoveProductFromCartCommand(UUID userId, UUID productId, int quantity) implements CartCommand {}


