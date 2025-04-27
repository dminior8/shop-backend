package pl.dminior8.cart_service.infrastructure.repository;

import pl.dminior8.cart_service.domain.model.Cart;

import java.util.Optional;

/**
 * Interfejs repozytorium domenowego koszyka.
 */
public interface CartRepository {

    Optional<Cart> findByUserId(String userId);

    Cart save(Cart cart);
}

