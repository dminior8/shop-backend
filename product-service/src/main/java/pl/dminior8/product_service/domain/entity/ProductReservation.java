package pl.dminior8.product_service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Entity
@Table(name = "product_reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID cartId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public ProductReservation(UUID cartId, UUID productId, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        this.cartId = cartId;
        this.productId = productId;
        this.quantity = qty;
        this.createdAt = now();
    }

    public void decreaseQuantity(int delta) {
        if (delta <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (quantity < delta) throw new IllegalStateException("Not enough stock");
        quantity -= delta;
    }

    public void increaseQuantity(int delta) {
        if (delta <= 0) throw new IllegalArgumentException("Quantity must be positive");
        quantity += delta;
    }
}

