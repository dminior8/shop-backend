package pl.dminior8.cart_service.infrastructure.external.messaging;

/**
 * Abstrakcja nad dowolnym systemem kolejkowym.
 */
public interface DomainEventPublisher {
    /**
     * Publikuje dowolne zdarzenie domenowe do brokera.
     */
    void publish(Object domainEvent);
}

