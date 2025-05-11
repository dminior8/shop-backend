package pl.dminior8.product_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dminior8.product_service.domain.entity.ProductReservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductReservationRepository extends JpaRepository<ProductReservation, UUID> {
    Optional<ProductReservation> findByCartIdAndProductId(UUID cartId, UUID productId);

    List<ProductReservation> findAllByCartId(UUID cartId);

    void deleteAllByCartId(UUID cartId);
}
