package pl.dminior8.common.exceptions.product;

import lombok.Getter;

@Getter
public class RedisKeyException extends RuntimeException {
  private final String key;

  public RedisKeyException(String key) {
    super(String.format("Wrong key format: %s", key));
      this.key = key;
  }
}
