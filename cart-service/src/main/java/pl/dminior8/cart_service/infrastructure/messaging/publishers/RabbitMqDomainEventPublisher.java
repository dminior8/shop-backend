package pl.dminior8.cart_service.infrastructure.messaging.publishers;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.stereotype.Component;
import pl.dminior8.common.event.*;

@Component
public class RabbitMqDomainEventPublisher implements DomainEventPublisher {
    private final RabbitTemplate rabbit;
    private final String cartEventsExchange;
    private final String orderEventsExchange;

    public static final String PRODUCT_RESERVED_ROUTING_KEY = "product.reserved";
    public static final String PRODUCT_REMOVED_ROUTING_KEY = "product.removed";
    public static final String CART_CHACKOUT_ROUTING_KEY = "cart.checkedout";
    public static final String CART_CREATED_ROUTING_KEY = "cart.created";
    public static final String PRODUCT_EXPIRED_ROUTING_KEY = "cart.expired";

    public RabbitMqDomainEventPublisher(RabbitTemplate rabbit,
                                        @Value("${messaging.exchange.cart-events}") String cartEventsExchange,
                                        @Value("${messaging.exchange.order-events}") String orderEventsExchange) {
        this.rabbit = rabbit;
        this.cartEventsExchange = cartEventsExchange;
        this.orderEventsExchange = orderEventsExchange;
    }

    @Override
    public void publish(Object domainEvent) {
        try {

            switch (domainEvent) {
                case ProductReservedEvent productReservedEvent ->
                        rabbit.convertAndSend(cartEventsExchange, PRODUCT_RESERVED_ROUTING_KEY, domainEvent);
                case ProductRemovedEvent productRemovedEvent ->
                        rabbit.convertAndSend(cartEventsExchange, PRODUCT_REMOVED_ROUTING_KEY, domainEvent);
                case CartCheckedOutEvent cartCheckedOutEvent ->
                        rabbit.convertAndSend(orderEventsExchange, CART_CHACKOUT_ROUTING_KEY, domainEvent);
                case CartCreatedEvent cartCreatedEvent ->
                        rabbit.convertAndSend(cartEventsExchange, CART_CREATED_ROUTING_KEY, domainEvent);
                case CartExpiredEvent cartExpiredEvent ->
                        rabbit.convertAndSend(cartEventsExchange, PRODUCT_EXPIRED_ROUTING_KEY, domainEvent);
                case null -> throw new IllegalArgumentException("Empty domainEvent");
                default -> throw new IllegalArgumentException("Unsupported event: " + domainEvent.getClass().getName());
            }

        } catch (Exception ex) {
            throw new SerializationFailedException("Event serialization error: " + domainEvent, ex);
        }
    }
}

