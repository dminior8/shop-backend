package pl.dminior8.cart_service.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

import java.util.UUID;

@Component
@Slf4j
public class CartExpiryListener {
    private final CartRepository repo;

    public CartExpiryListener(CartRepository repo) {
        this.repo = repo;
    }

    public void onMessage(String expiredKey) {
        try {
            String[] parts = expiredKey.split(":");
            if (parts.length >= 2) {
                String userId = parts[1]; // Zakładając format "cart:<userId>"
                repo.findByUserId(UUID.fromString(userId)).ifPresent(cart -> {
                    cart.expire();
                    repo.save(cart);
                    log.info("Expired cart for user: {}", userId);
                });
            } else {
                log.warn("Unexpected key format: {}", expiredKey);
            }
        } catch (Exception e) {
            log.error("Error processing expired key: " + expiredKey, e);
        }
    }
}

