package pl.dminior8.cart_service.infrastructure.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.domain.event.CartCheckedOutEvent;
import pl.dminior8.cart_service.domain.event.ProductReservedEvent;

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

            if (domainEvent instanceof ProductReservedEvent) {
                // exchange, routingKey (tutaj pusta), obiekt payload
                rabbit.convertAndSend(cartEventsExchange, "", payload);
            }
            else if (domainEvent instanceof CartCheckedOutEvent) {
                rabbit.convertAndSend(orderEventsExchange, "", payload);
            }
            else {
                throw new IllegalArgumentException("Nieobsługiwany event: " + domainEvent.getClass());
            }

        } catch (Exception ex) {
            throw new RuntimeException("Błąd serializacji zdarzenia: " + domainEvent, ex);
        }
    }
}

