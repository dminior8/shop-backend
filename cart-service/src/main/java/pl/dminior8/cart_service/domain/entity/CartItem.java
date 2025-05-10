package pl.dminior8.cart_service.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cart_id")
    private UUID cartId;

    private UUID productId;

    @Min(0)
    private int quantity;

    @Min(0)
    private double price;

    private Instant addedAt;

    protected CartItem() {
    }

    public CartItem(UUID cartId, UUID productId, int qty, double price) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = qty;
        this.price = price;
        this.addedAt = Instant.now();
    }

    public void increaseQuantity(int delta) {
        this.quantity += delta;
    }

    public void decreaseQuantity(int delta) {
        this.quantity -= delta;
    }
}

