package pl.dminior8.cart_service.infrastructure.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMqConfig {

    @Value("${messaging.exchange.cart-events}")
    private String cartExchange;

    @Value("${messaging.exchange.order-events}")
    private String orderExchange;

    // Wymiana zdarzeń
    @Bean
    public TopicExchange cartEventsExchange() {
        return new TopicExchange(cartExchange, true, false);
    }

    @Bean
    public DirectExchange orderEventsExchange() {
        return new DirectExchange(orderExchange, true, false);
    }

    // Kolejki dla wszystkich zdarzeń
    @Bean
    public Queue cartCreatedQueue() {
        return new Queue("cart.created.queue", true);
    }

    @Bean
    public Queue productReservedQueue() {
        return new Queue("product.reserved.queue", true);
    }

    @Bean
    public Queue productRemovedQueue() {
        return new Queue("product.removed.queue", true);
    }

    @Bean
    public Queue cartExpiredQueue() {
        return new Queue("cart.expired.queue", true);
    }

    @Bean
    public Queue cartCheckedOutQueue() {
        return new Queue("cart.checkedout.queue", true);
    }

    // Bindingi dla cartEventsExchange
    @Bean
    public Binding cartCreatedBinding() {
        return BindingBuilder.bind(cartCreatedQueue())
                .to(cartEventsExchange())
                .with("cart.created");
    }

    @Bean
    public Binding productReservedBinding() {
        return BindingBuilder.bind(productReservedQueue())
                .to(cartEventsExchange())
                .with("product.reserved");
    }

    @Bean
    public Binding productRemovedBinding() {
        return BindingBuilder.bind(productRemovedQueue())
                .to(cartEventsExchange())
                .with("product.removed");
    }

    @Bean
    public Binding cartExpiredBinding() {
        return BindingBuilder.bind(cartExpiredQueue())
                .to(cartEventsExchange())
                .with("cart.expired");
    }

    // Bindingi dla orderEventsExchange
    @Bean
    public Binding cartCheckedOutBinding() {
        return BindingBuilder.bind(cartCheckedOutQueue())
                .to(orderEventsExchange())
                .with("cart.checkedout");
    }

    // Pozostała konfiguracja
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri("amqp://guest:guest@rabbitmq:5672");
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Declarables declarables() {
        return new Declarables(
                cartEventsExchange(),
                orderEventsExchange(),
                cartCreatedQueue(),
                productReservedQueue(),
                productRemovedQueue(),
                cartExpiredQueue(),
                cartCheckedOutQueue()
        );
    }
}
