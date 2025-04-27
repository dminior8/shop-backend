package pl.dminior8.cart_service.infrastructure.config;

import org.redisson.api.RedissonClient;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguracja Redisson do blokad rozproszonych (Redlock).
 */
@Configuration
public class LockingConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config cfg = new Config();
        cfg.useSingleServer()
                .setAddress("redis://redis:6379"); // nazwa hosta z docker-compose
        return Redisson.create(cfg);
    }
}

