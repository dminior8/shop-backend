package pl.dminior8.cart_service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dminior8.cart_service.domain.model.Cart;

import java.util.Optional;

public interface CartEntityRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
