package pl.dminior8.product_service.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Min(0)
    private double price;

    @Column(name = "available_qty", nullable = false)
    @Min(0)
    private int availableQuantity;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public void decreaseQuantity(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (availableQuantity < qty) throw new IllegalStateException("Not enough stock");
        availableQuantity -= qty;
        updatedAt = Instant.now();
    }

    public void increaseQuantity(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        availableQuantity += qty;
        updatedAt = Instant.now();
    }
}

