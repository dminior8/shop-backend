package pl.dminior8.product_service.application.productReservation.command.reserveProduct;


import java.util.UUID;

public record ReserveProductCommand(UUID productId, UUID cartId, int quantity) {}