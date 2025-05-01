package pl.dminior8.cart_service.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.shoppingCart.query.GetCart.GetCartQuery;
import pl.dminior8.cart_service.application.shoppingCart.query.GetCart.GetCartQueryHandler;
import pl.dminior8.cart_service.application.shoppingCart.query.GetCartTotalValue.GetCartTotalValueQuery;
import pl.dminior8.cart_service.application.shoppingCart.query.GetCartTotalValue.GetCartTotalValueQueryHandler;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.application.common.dto.CartDto;
import pl.dminior8.cart_service.application.common.mapper.CartDtoMapper;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/user/{userId}/cart")
public class CartQueryController {

    private final GetCartQueryHandler getCart;
    private final GetCartTotalValueQueryHandler getTotal;
    private final CartDtoMapper cartDtoMapper;

    public CartQueryController(GetCartQueryHandler getCart,
                               GetCartTotalValueQueryHandler getTotal, CartDtoMapper cartDtoMapper) {
        this.getCart = getCart;
        this.getTotal = getTotal;
        this.cartDtoMapper = cartDtoMapper;
    }

    @GetMapping()
    public ResponseEntity<CartDto> getCart(@PathVariable UUID userId) {
        Cart cart = getCart.handle(new GetCartQuery(userId));
        return ResponseEntity.ok(cartDtoMapper.toCartDto(cart));
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotal(@PathVariable UUID userId) {
        double sum = getTotal.handle(new GetCartTotalValueQuery(userId));
        return ResponseEntity.ok(sum);
    }
}

