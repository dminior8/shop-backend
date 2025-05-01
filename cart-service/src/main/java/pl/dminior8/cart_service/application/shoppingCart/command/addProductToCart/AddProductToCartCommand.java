package pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart;

import lombok.Builder;
import pl.dminior8.cart_service.application.shoppingCart.command.CartCommand;

import java.util.UUID;


@Builder
public record AddProductToCartCommand(UUID userId, UUID productId, int quantity) implements CartCommand {}
