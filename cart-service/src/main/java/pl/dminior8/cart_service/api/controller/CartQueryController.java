package pl.dminior8.cart_service.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.cart_service.application.shoppingCart.query.getCartByCartId.GetCartByCartIdQuery;
import pl.dminior8.cart_service.application.shoppingCart.query.getCartByCartId.GetCartByCartIdQueryHandler;
import pl.dminior8.cart_service.application.shoppingCart.query.getCartByUserId.GetCartByUserIdQuery;
import pl.dminior8.cart_service.application.shoppingCart.query.getCartByUserId.GetCartByUserIdQueryHandler;
import pl.dminior8.cart_service.application.shoppingCart.query.getCartTotalValue.GetCartTotalValueQuery;
import pl.dminior8.cart_service.application.shoppingCart.query.getCartTotalValue.GetCartTotalValueQueryHandler;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.common.dto.CartDto;
import pl.dminior8.cart_service.application.common.mapper.CartDtoMapper;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/{userId}/cart")
public class CartQueryController {

    private final GetCartByUserIdQueryHandler getCartByUserId;
    private final GetCartByCartIdQueryHandler getCartByCartId;
    private final GetCartTotalValueQueryHandler getTotal;
    private final CartDtoMapper cartDtoMapper;

    public CartQueryController(GetCartByUserIdQueryHandler getCartByUserId,
                               GetCartByCartIdQueryHandler getCartByCartId,
                               GetCartTotalValueQueryHandler getTotal,
                               CartDtoMapper cartDtoMapper) {
        this.getCartByUserId = getCartByUserId;
        this.getCartByCartId = getCartByCartId;
        this.getTotal = getTotal;
        this.cartDtoMapper = cartDtoMapper;
    }

    @GetMapping()
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable UUID userId) {
        Cart cart = getCartByUserId.handle(new GetCartByUserIdQuery(userId));
        return ResponseEntity.ok(cartDtoMapper.toCartDto(cart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable UUID userId, @PathVariable UUID cartId) {
        Cart cart = getCartByCartId.handle(new GetCartByCartIdQuery(userId, cartId));
        return ResponseEntity.ok(cartDtoMapper.toCartDto(cart));
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotal(@PathVariable UUID userId) {
        double sum = getTotal.handle(new GetCartTotalValueQuery(userId));
        return ResponseEntity.ok(sum);
    }
}

