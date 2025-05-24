package pl.dminior8.cart_service.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

import static pl.dminior8.cart_service.domain.entity.CartItemStatus.PROCESSING;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {
    @Id
    private UUID id;

    @Column(name = "cart_id")
    private UUID cartId;

    private UUID productId;

    @Min(0)
    private int quantity;

    @Min(0)
    private double price;

    @Enumerated(EnumType.STRING)
    private CartItemStatus status;

    private Instant addedAt;

    protected CartItem() {
    }

    public CartItem(UUID cartId, UUID productId, int qty, double price) {
        this.id = UUID.randomUUID();
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = qty;
        this.price = price;
        this.status = PROCESSING;
        this.addedAt = Instant.now();
    }

    public void increaseQuantity(int delta) {
        this.quantity += delta;
    }

    public void decreaseQuantity(int delta) {
        this.quantity -= delta;
    }
}

