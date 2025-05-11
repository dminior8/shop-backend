package pl.dminior8.order_service.infrastructure.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${messaging.exchange.order-events}")
    private String orderEventsExchange;

    @Bean
    public Queue cartCheckedOutQueue() {
        return new Queue("cart.checkedout.queue", true);
    }

    @Bean
    public DirectExchange orderEventsExchange() {
        return new DirectExchange(orderEventsExchange);
    }

    @Bean
    public Binding cartCheckedOutBinding(Queue cartCheckedOutQueue, DirectExchange orderEventsExchange) {
        return BindingBuilder.bind(cartCheckedOutQueue)
                .to(orderEventsExchange)
                .with("cart.checkedout");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
