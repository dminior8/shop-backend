package pl.dminior8.cart_service.infrastructure.messaging.publishers;

/**
 * Abstrakcja nad dowolnym systemem kolejkowym.
 */
public interface DomainEventPublisher {
    /**
     * Publikuje dowolne zdarzenie domenowe do brokera.
     */
    void publish(Object domainEvent);
}

