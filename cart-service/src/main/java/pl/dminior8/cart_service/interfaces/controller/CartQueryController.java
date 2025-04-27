package pl.dminior8.cart_service.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.query.GetCartQueryHandler;
import pl.dminior8.cart_service.application.query.GetCartTotalValueQueryHandler;
import pl.dminior8.cart_service.domain.model.Cart;

@RestController
@RequestMapping("/api/cart")
public class CartQueryController {

    private final GetCartQueryHandler getCart;
    private final GetCartTotalValueQueryHandler getTotal;

    public CartQueryController(GetCartQueryHandler getCart,
                               GetCartTotalValueQueryHandler getTotal) {
        this.getCart = getCart;
        this.getTotal = getTotal;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Cart cart = getCart.handle(new GetCartQuery(userId));
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> getTotal(@PathVariable String userId) {
        double sum = getTotal.handle(new GetCartTotalValueQuery(userId));
        return ResponseEntity.ok(sum);
    }
}

