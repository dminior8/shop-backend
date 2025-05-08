package pl.dminior8.product_service.application.product.query.getProduct;

import java.util.Objects;
import java.util.UUID;

public record GetProductQuery(UUID productId) {
    public GetProductQuery {
        Objects.requireNonNull(productId, "productId must not be null");
        if (productId.toString().isEmpty()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
    }
}