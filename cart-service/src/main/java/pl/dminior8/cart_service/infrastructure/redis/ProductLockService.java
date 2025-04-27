package pl.dminior8.cart_service.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class ProductLockService {

    private static final Duration LOCK_TTL = Duration.ofMinutes(15);

    private final RedisTemplate<String, Object> redis;

    public ProductLockService(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    public boolean tryLockProduct(UUID productId, UUID cartId) {
        String key = keyFor(productId);
        ValueOperations<String, Object> ops = redis.opsForValue();

        // setIfAbsent => setnx (SET if Not eXists) + ttl atomowo
        Boolean success = ops.setIfAbsent(key, cartId.toString(), LOCK_TTL);
        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(UUID productId, UUID cartId) {
        String key = keyFor(productId);
        ValueOperations<String, Object> ops = redis.opsForValue();
        String owner = (String) ops.get(key);
        if (cartId.toString().equals(owner)) {
            redis.delete(key);
        }
    }

    private String keyFor(UUID productId) {
        return "lock:product:" + productId;
    }
}
