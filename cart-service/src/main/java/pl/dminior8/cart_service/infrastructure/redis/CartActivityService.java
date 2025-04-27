package pl.dminior8.cart_service.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Component
public class CartActivityService {

    private static final Duration CART_TTL = Duration.ofMinutes(15);

    private final RedisTemplate<String, Object> redis;

    public CartActivityService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

     // Odświeża Time To Live przy każdej operacji
    public void refreshCartTtl(UUID cartId) {
        String key = keyFor(cartId);
        redis.opsForValue().set(key, System.currentTimeMillis(), CART_TTL);
    }

    public boolean exists(UUID cartId) {
        return TRUE.equals(redis.hasKey(keyFor(cartId)));
    }

    private String keyFor(UUID cartId) {
        return "cart:active:" + cartId;
    }
}

