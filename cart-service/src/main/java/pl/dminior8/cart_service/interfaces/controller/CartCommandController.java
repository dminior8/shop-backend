package pl.dminior8.cart_service.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.command.AddProductToCart.AddProductToCartCommandHandler;
import pl.dminior8.cart_service.application.command.CartCommand;
import pl.dminior8.cart_service.application.command.CheckoutCart.CheckoutCartCommandHandler;
import pl.dminior8.cart_service.application.command.CreateCart.CreateCartCommandHandler;
import pl.dminior8.cart_service.application.command.RemoveProductFromCart.RemoveProductFromCartCommandHandler;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartCommandController {

    private final AddProductToCartCommandHandler addHandler;
    private final RemoveProductFromCartCommandHandler removeHandler;
    private final CreateCartCommandHandler createHandler;
    private final CheckoutCartCommandHandler checkoutHandler;

    public CartCommandController(AddProductToCartCommandHandler addHandler,
                                 RemoveProductFromCartCommandHandler removeHandler,
                                 CreateCartCommandHandler createHandler,
                                 CheckoutCartCommandHandler checkoutHandler) {
        this.addHandler = addHandler;
        this.removeHandler = removeHandler;
        this.createHandler = createHandler;
        this.checkoutHandler = checkoutHandler;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> createCart(@PathVariable UUID userId) {
        createHandler.handle(CartCommand.builder().userId(userId).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Void> addProduct(@PathVariable UUID userId,
                                           @RequestParam UUID productId,
                                           @RequestParam int quantity) {
        addHandler.handle(CartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Void> removeProduct(@PathVariable UUID userId,
                                              @RequestParam UUID productId,
                                              @RequestParam int quantity) {
        removeHandler.handle(CartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable UUID userId) {
        checkoutHandler.handle(CartCommand.builder().userId(userId).build());
        return ResponseEntity.ok().build();
    }
}

