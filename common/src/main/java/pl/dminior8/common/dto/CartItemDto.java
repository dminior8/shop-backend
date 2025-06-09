package pl.dminior8.common.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemDto {
    private UUID id;
    private UUID cartId;

    @Column(nullable = false)
    private UUID productId;

    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}

