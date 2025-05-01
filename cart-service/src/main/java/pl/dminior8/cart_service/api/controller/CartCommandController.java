package pl.dminior8.cart_service.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart.AddProductToCartCommandHandler;
import pl.dminior8.cart_service.application.shoppingCart.command.addProductToCart.AddProductToCartCommand;
import pl.dminior8.cart_service.application.shoppingCart.command.checkoutCart.CheckoutCartCommandHandler;
import pl.dminior8.cart_service.application.shoppingCart.command.createCart.CreateCartCommand;
import pl.dminior8.cart_service.application.shoppingCart.command.createCart.CreateCartCommandHandler;
import pl.dminior8.cart_service.application.shoppingCart.command.removeProductFromCart.RemoveProductFromCartCommandHandler;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/{userId}/cart")
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

    @PostMapping()
    public ResponseEntity<Void> createCart(@PathVariable UUID userId) {
        createHandler.handle(CreateCartCommand.builder().userId(userId).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-product")
    public ResponseEntity<Void> addProduct(@PathVariable UUID userId,
                                           @RequestParam UUID productId,
                                           @RequestParam int quantity) {
        addHandler.handle(AddProductToCartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<Void> removeProduct(@PathVariable UUID userId,
                                              @RequestParam UUID productId,
                                              @RequestParam int quantity) {
        removeHandler.handle(AddProductToCartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@PathVariable UUID userId) {
        checkoutHandler.handle(AddProductToCartCommand.builder().userId(userId).build());
        return ResponseEntity.ok().build();
    }
}

