package pl.dminior8.cart_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @GeneratedValue
    private UUID id;
    private UUID productId;
    private int quantity;
    private float price;
    private Instant addedAt;

    protected CartItem() {}
    public CartItem(UUID productId, int qty, Instant addedAt) {
        this.productId = productId;
        this.quantity = qty;
        this.addedAt = addedAt;
    }

    public void increaseQuantity(int delta) {
        this.quantity += delta;
    }

    public void decreaseQuantity(int delta) {
        this.quantity -= delta;
    }
}

