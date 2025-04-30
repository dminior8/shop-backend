package pl.dminior8.cart_service.application.cart.command.AddProductToCart;

import lombok.Builder;
import pl.dminior8.cart_service.application.cart.command.CartCommand;

import java.util.UUID;


@Builder
public record AddProductToCartCommand(UUID userId, UUID productId, int quantity) implements CartCommand {}
