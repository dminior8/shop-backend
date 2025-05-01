package pl.dminior8.cart_service.application.shoppingCart.command.checkoutCart;


import lombok.Builder;
import pl.dminior8.cart_service.application.shoppingCart.command.CartCommand;

import java.util.UUID;


@Builder
public record CheckoutCartCommand(UUID userId) implements CartCommand {}

