package pl.dminior8.cart_service.domain.model;

import jakarta.persistence.*;
import pl.dminior8.cart_service.domain.event.CartCheckedOut;
import pl.dminior8.cart_service.domain.event.ProductRemoved;
import pl.dminior8.cart_service.domain.event.ProductReserved;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING) private CartStatus status = CartStatus.ACTIVE;
    private Instant createdAt = Instant.now();

    private Instant lastModifiedAt = Instant.now();

    @Version
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    // fabryka
    public static Cart create(UUID userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        cart.status = CartStatus.ACTIVE;
        cart.createdAt = Instant.now();
        cart.lastModifiedAt = Instant.now();
        /* cart.domainEvents.add(new ProductReserved(tu ewentualne eventy inicjalizacyjne ));*/
        return cart;
    }

    public void addProduct(UUID productId, int quantity) {
        ensureActive();
        CartItem existing = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.increaseQuantity(quantity);
        } else {
            items.add(new CartItem(productId, quantity, Instant.now()));
        }
        this.lastModifiedAt = Instant.now();
        // rejestrujemy zdarzenie
        this.domainEvents.add(new ProductReserved(this.id, productId, quantity));
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
            // usuwamy całą pozycję
            items.remove(existing);
            domainEvents.add(new ProductRemoved(this.id, productId, existing.getQuantity()));
        } else {
            // zmniejszamy ilość
            existing.decreaseQuantity(quantity);
            domainEvents.add(new ProductRemoved(this.id, productId, quantity));
        }
        this.lastModifiedAt = Instant.now();
    }

    // finalizacja koszyka
    public void checkout() {
        ensureActive();
        this.status = CartStatus.CHECKED_OUT;
        this.lastModifiedAt = Instant.now();
        domainEvents.add(new CartCheckedOut(this.id, this.userId));
    }

    private void ensureActive() {
        if (this.status != CartStatus.ACTIVE) {
            throw new IllegalStateException("Cart is not active: " + status);
        }
    }

    public List<Object> pullDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

}
