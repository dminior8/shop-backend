package pl.dminior8.cart_service.infrastructure.messaging.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;
import pl.dminior8.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import pl.dminior8.common.exceptions.product.RedisKeyException;

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

        if (!expiredKey.startsWith("cart:")){
            log.info("Wrong expired key format");
            throw new RedisKeyException(expiredKey);
        }

        UUID cartId = UUID.fromString(expiredKey.replace("cart:active:", ""));
        cartRepository.findById(cartId).ifPresent(cart -> {
            productClient.releaseByCart(cart.getId());
            cart.expire();
            cartRepository.save(cart);
            log.info("Released products and deleted cart for userId={}", cart.getUserId());
        });
    }
}



