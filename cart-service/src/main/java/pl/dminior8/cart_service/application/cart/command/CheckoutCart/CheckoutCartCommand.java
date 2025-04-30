package pl.dminior8.cart_service.application.cart.command.CheckoutCart;


import lombok.Builder;
import pl.dminior8.cart_service.application.cart.command.CartCommand;

import java.util.UUID;


@Builder
public record CheckoutCartCommand(UUID userId) implements CartCommand {}

