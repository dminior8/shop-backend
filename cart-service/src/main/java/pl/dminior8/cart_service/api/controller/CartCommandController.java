package pl.dminior8.cart_service.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.cart.command.AddProductToCart.AddProductToCartCommandHandler;
import pl.dminior8.cart_service.application.cart.command.AddProductToCart.AddProductToCartCommand;
import pl.dminior8.cart_service.application.cart.command.CheckoutCart.CheckoutCartCommandHandler;
import pl.dminior8.cart_service.application.cart.command.CreateCart.CreateCartCommandHandler;
import pl.dminior8.cart_service.application.cart.command.RemoveProductFromCart.RemoveProductFromCartCommandHandler;

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

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> createCart(@PathVariable UUID userId) {
        createHandler.handle(AddProductToCartCommand.builder().userId(userId).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}/add-product")
    public ResponseEntity<Void> addProduct(@PathVariable UUID userId,
                                           @RequestParam UUID productId,
                                           @RequestParam int quantity) {
        addHandler.handle(AddProductToCartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}/remove-product")
    public ResponseEntity<Void> removeProduct(@PathVariable UUID userId,
                                              @RequestParam UUID productId,
                                              @RequestParam int quantity) {
        removeHandler.handle(AddProductToCartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable UUID userId) {
        checkoutHandler.handle(AddProductToCartCommand.builder().userId(userId).build());
        return ResponseEntity.ok().build();
    }
}

