package pl.dminior8.product_service.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${messaging.exchange.cart-events}")
    private String cartExchangeName;

    @Value("${messaging.exchange.order-events}")
    private String orderExchangeName;

    public static final String PRODUCT_RESERVED_QUEUE = "product.reserved.queue";
    public static final String PRODUCT_REMOVED_QUEUE = "product.removed.queue";


    // Giełdy (Exchanges)
    @Bean
    public TopicExchange cartEventsExchange() {
        return new TopicExchange(cartExchangeName, true, false);
    }

    @Bean
    public DirectExchange orderEventsExchange() {
        return new DirectExchange(orderExchangeName, true, false);
    }

    @Bean
    public Queue productReservedQueue() {
        return new Queue(PRODUCT_RESERVED_QUEUE, true);
    }

    @Bean
    public Queue productRemovedQueue() {
        return new Queue(PRODUCT_REMOVED_QUEUE, true);
    }

    @Bean
    public Binding productReservedBinding(Queue productReservedQueue, TopicExchange cartEventsExchange) {
        return BindingBuilder.bind(productReservedQueue)
                .to(cartEventsExchange)
                .with("product.reserved");
    }

    @Bean
    public Binding productRemovedBinding(Queue productRemovedQueue, TopicExchange cartEventsExchange) {
        return BindingBuilder.bind(productRemovedQueue)
                .to(cartEventsExchange)
                .with("product.removed");
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        // Wstrzyknięcie ObjectMappera zarządzanego przez Springa
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter); // Użycie skonfigurowanego konwertera
        return template;
    }

    @Bean
    public Declarables declarables(TopicExchange cartEventsExchange,
                                   DirectExchange orderEventsExchange,
                                   Queue productReservedQueue,
                                   Queue productRemovedQueue,
                                   Binding productReservedBinding,
                                   Binding productRemovedBinding
    ) {
        return new Declarables(
                cartEventsExchange,
                orderEventsExchange,
                productReservedQueue,
                productRemovedQueue,
                productReservedBinding,
                productRemovedBinding
        );
    }
}
