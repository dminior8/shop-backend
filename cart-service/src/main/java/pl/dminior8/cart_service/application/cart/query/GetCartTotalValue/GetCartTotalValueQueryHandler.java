package pl.dminior8.cart_service.application.cart.query.GetCartTotalValue;


import org.springframework.stereotype.Component;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.infrastructure.repository.CartRepository;

@Component
public class GetCartTotalValueQueryHandler {

    private final CartRepository cartRepo;

    public GetCartTotalValueQueryHandler(CartRepository cartRepo) {
        this.cartRepo = cartRepo;
    }

    public double handle(GetCartTotalValueQuery query) {
        Cart cart = cartRepo.findByUserId(query.userId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return cart.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}