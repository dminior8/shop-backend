package pl.dminior8.cart_service.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;

import java.util.UUID;


@Slf4j
@Component
public class CartExpirationListener implements MessageListener {

    private final CartRepository cartRepository;
    private final ExternalProductServiceClient productClient;

    public CartExpirationListener(CartRepository cartRepository, ExternalProductServiceClient productClient) {
        this.cartRepository = cartRepository;
        this.productClient = productClient;
    }

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        log.info("Expired Redis key: {}", expiredKey);

        if (!expiredKey.startsWith("cart:")) return;

        UUID userId = UUID.fromString(expiredKey.replace("cart:active:", ""));
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            productClient.releaseByCart(cart.getId());
            cartRepository.delete(cart);
            log.info("Released products & deleted cart for userId={}", userId);
        });
    }
}



