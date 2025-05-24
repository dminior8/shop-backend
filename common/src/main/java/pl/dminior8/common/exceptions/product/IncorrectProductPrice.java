package pl.dminior8.common.exceptions.product;

import lombok.Getter;

import java.util.UUID;

@Getter
public class IncorrectProductPrice extends RuntimeException {
  private final UUID productId;
  private final double price;

  public IncorrectProductPrice(UUID productId, double price) {
    super(String.format(
            "Product %s not available in requested price: %s",
            productId,
            price
    ));
    this.productId = productId;
    this.price = price;
  }
}
