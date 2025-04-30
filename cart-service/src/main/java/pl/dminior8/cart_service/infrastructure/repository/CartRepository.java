package pl.dminior8.cart_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dminior8.cart_service.domain.entity.Cart;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfejs repozytorium domenowego koszyka.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);

    Cart save(Cart cart);
}

