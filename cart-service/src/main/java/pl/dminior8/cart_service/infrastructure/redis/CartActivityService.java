package pl.dminior8.cart_service.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Component
@Slf4j
public class CartActivityService {

    private static final Duration CART_TTL = Duration.ofMinutes(1);

    private final RedisTemplate<String, Object> redis;

    public CartActivityService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

     // Odświeża Time To Live przy każdej operacji
    public String refreshCartTtl(UUID cartId) {
        String key = "cart:active:" + cartId;
        redis.opsForValue().set(key, System.currentTimeMillis(), CART_TTL);
        log.info("Refreshed TTL of " + key);
        return key;
    }

    public String expireCartTtl(UUID cartId) {
        String key = "cart:active:" + cartId;
        redis.expire(key, CART_TTL);
        log.info("Expired TTL of " + key);
        return key;
    }

    public boolean exists(UUID cartId) {
        return TRUE.equals(redis.hasKey("cart:active:" + cartId));
    }
}