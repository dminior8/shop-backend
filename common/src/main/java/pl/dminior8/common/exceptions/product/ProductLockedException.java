package pl.dminior8.common.exceptions.product;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductLockedException extends RuntimeException {

    public ProductLockedException(UUID productId) {
        super(String.format("Product [id: %s] is locked.", productId));
    }
}
