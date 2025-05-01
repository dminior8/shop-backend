package pl.dminior8.common.exceptions.product;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductNotAvailableException extends RuntimeException {
  private final UUID productId;
  private final int requestedQuantity;
  private final int availableQuantity;

  public ProductNotAvailableException(UUID productId, int requestedQuantity, int availableQuantity) {
    super(String.format(
            "Product %s not available in requested quantity. Requested: %d, Available: %d",
            productId, requestedQuantity, availableQuantity
    ));
    this.productId = productId;
    this.requestedQuantity = requestedQuantity;
    this.availableQuantity = availableQuantity;
  }
}
