package pl.dminior8.product_service.application.productReservation.command.releaseProductByCart;


import java.util.UUID;

public record ReleaseProductByCartCommand(UUID cartId) {}

