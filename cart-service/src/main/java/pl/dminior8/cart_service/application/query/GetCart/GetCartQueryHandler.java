package pl.dminior8.cart_service.application.query.GetCart;

import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.domain.model.Cart;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class GetCartQueryHandler {

    private final CartRepository cartRepo;

    public GetCartQueryHandler(CartRepository cartRepo) {
        this.cartRepo = cartRepo;
    }

    public Cart handle(GetCartQuery query) {
        return cartRepo.findByUserId(query.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }
}

