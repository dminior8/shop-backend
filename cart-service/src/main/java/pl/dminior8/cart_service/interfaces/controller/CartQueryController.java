package pl.dminior8.cart_service.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.query.GetCart.GetCartQuery;
import pl.dminior8.cart_service.application.query.GetCart.GetCartQueryHandler;
import pl.dminior8.cart_service.application.query.GetCartTotalValue.GetCartTotalValueQuery;
import pl.dminior8.cart_service.application.query.GetCartTotalValue.GetCartTotalValueQueryHandler;
import pl.dminior8.cart_service.domain.model.Cart;
import pl.dminior8.cart_service.interfaces.dto.CartDto;
import pl.dminior8.cart_service.interfaces.mapper.CartDtoMapper;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/cart")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID userId) {
        Cart cart = getCart.handle(new GetCartQuery(userId));
        return ResponseEntity.ok(cartDtoMapper.toCartDto(cart));
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> getTotal(@PathVariable UUID userId) {
        double sum = getTotal.handle(new GetCartTotalValueQuery(userId));
        return ResponseEntity.ok(sum);
    }
}

