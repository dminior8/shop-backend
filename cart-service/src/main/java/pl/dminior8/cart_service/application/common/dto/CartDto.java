package pl.dminior8.cart_service.application.common.dto;

import lombok.Getter;
import lombok.Setter;
import pl.dminior8.cart_service.domain.entity.CartItem;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartDto {
    private UUID id;

    private UUID userId;

    private Instant createdAt = Instant.now();

    private Instant lastModifiedAt = Instant.now();

    private List<CartItem> items = new ArrayList<>();
}
