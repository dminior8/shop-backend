package pl.dminior8.product_service.application.productReservation.command.releaseProduct;


import java.util.UUID;

public record ReleaseProductCommand(UUID productId, UUID cartId, int quantity) {}

