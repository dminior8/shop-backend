package pl.dminior8.cart_service.infrastructure.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.domain.event.*;

@Component
public class RabbitMqDomainEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper;
    private final String cartEventsExchange;
    private final String orderEventsExchange;

    public RabbitMqDomainEventPublisher(RabbitTemplate rabbit,
                                        ObjectMapper mapper,
                                        @Value("${messaging.exchange.cart-events}") String cartEventsExchange,
                                        @Value("${messaging.exchange.order-events}") String orderEventsExchange) {
        this.rabbit = rabbit;
        this.mapper = mapper;
        this.cartEventsExchange = cartEventsExchange;
        this.orderEventsExchange = orderEventsExchange;
    }

    @Override
    public void publish(Object domainEvent) {
        try {
            String payload = mapper.writeValueAsString(domainEvent);

            switch (domainEvent) {
                case ProductReservedEvent productReservedEvent ->
                        rabbit.convertAndSend(cartEventsExchange, "product.reserved", payload);
                case ProductRemovedEvent productRemovedEvent ->
                        rabbit.convertAndSend(cartEventsExchange, "product.removed", payload);
                case CartCheckedOutEvent cartCheckedOutEvent ->
                        rabbit.convertAndSend(orderEventsExchange, "cart.checkedout", payload);
                case CartCreatedEvent cartCreatedEvent ->
                        rabbit.convertAndSend(cartEventsExchange, "cart.created", payload);
                case CartExpiredEvent cartExpiredEvent ->
                        rabbit.convertAndSend(cartEventsExchange, "cart.expired", payload);
                case null, default -> {
                    assert domainEvent != null;
                    throw new IllegalArgumentException("Unsupported event: " + domainEvent.getClass());
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("Event serialization error: " + domainEvent, ex);
        }
    }
}

