package pl.dminior8.cart_service.infrastructure.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

// Konfiguracja wygasłych kluczy Ex
@Configuration
public class RedisKeyspaceConfig {
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory cf,
                                            MessageListenerAdapter adapter) {
        RedisMessageListenerContainer c = new RedisMessageListenerContainer();
        c.setConnectionFactory(cf);
        c.addMessageListener(adapter, new PatternTopic("__keyevent@0__:expired"));
        return c;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(CartExpiryListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}