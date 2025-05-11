package pl.dminior8.cart_service.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pl.dminior8.cart_service.domain.event.CartCheckedOutEvent;
import pl.dminior8.cart_service.domain.event.CartExpiredEvent;
import pl.dminior8.cart_service.domain.event.ProductRemovedEvent;
import pl.dminior8.cart_service.domain.event.ProductReservedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.ACTIVE;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @Version
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    @Transient //pomija pole w zapisie do bazy (zdarzenia domenowe publikowane za pomocą publishera na kolejkę)
    private final List<Object> domainEvents = new ArrayList<>();

    // fabryka
    public static Cart create(UUID userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        cart.status = CartStatus.ACTIVE;
        cart.createdAt = Instant.now();
        cart.updatedAt = Instant.now();

        return cart;
    }

    public void addProduct(UUID productId, int quantity, double price) {
        ensureActive();
        CartItem existing = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.increaseQuantity(quantity);
        } else {
            items.add(new CartItem(id, productId, quantity, price));
        }
        this.updatedAt = Instant.now();
        // rejestracja zdarzenia
        this.domainEvents.add(new ProductReservedEvent(this.id, productId, quantity));
    }

    public void removeProduct(UUID productId, int quantity) {
        ensureActive();
        CartItem existing = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product " + productId + " not in cart"));

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity >= existing.getQuantity()) {
            items.remove(existing);
            domainEvents.add(new ProductRemovedEvent(this.id, productId, existing.getQuantity()));
        } else {
            existing.decreaseQuantity(quantity);
            domainEvents.add(new ProductRemovedEvent(this.id, productId, quantity));
        }
        this.updatedAt = Instant.now();
    }

    // finalizacja koszyka
    public void checkout() {
        ensureActive();
        this.status = CartStatus.CHECKED_OUT;
        this.updatedAt = Instant.now();
        domainEvents.add(new CartCheckedOutEvent(this.id, this.userId));
    }

    private void ensureActive() {
        if (this.status != CartStatus.ACTIVE) {
            throw new IllegalStateException("Cart is not active: " + status);
        }
    }

    //metoda czyszcząca listę domainEvents po publikacji
    public List<Object> pullDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    public void expire() {
        if (this.status != CartStatus.ACTIVE) return;
        this.status = CartStatus.EXPIRED;
        this.updatedAt = Instant.now();
        this.domainEvents.add(new CartExpiredEvent(this.id, this.userId));
    }
}
