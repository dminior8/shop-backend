package pl.dminior8.product_service.api.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.product_service.application.common.mapper.ProductDtoMapper;
import pl.dminior8.product_service.application.product.query.getProduct.GetProductQuery;
import pl.dminior8.product_service.application.product.query.getProduct.GetProductQueryHandler;
import pl.dminior8.product_service.application.productReservation.command.releaseProduct.ReleaseProductCommand;
import pl.dminior8.product_service.application.productReservation.command.releaseProduct.ReleaseProductCommandHandler;
import pl.dminior8.product_service.application.productReservation.command.releaseProductByCart.ReleaseProductByCartCommand;
import pl.dminior8.product_service.application.productReservation.command.releaseProductByCart.ReleaseProductByCartCommandHandler;
import pl.dminior8.product_service.application.productReservation.command.reserveProduct.ReserveProductCommand;
import pl.dminior8.product_service.application.productReservation.command.reserveProduct.ReserveProductCommandHandler;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductCommandController {
    private final ReserveProductCommandHandler reserveHandler;
    private final ReleaseProductCommandHandler releaseProductHandler;
    private final ReleaseProductByCartCommandHandler releaseByCartHandler;
    private final GetProductQueryHandler queryHandler;
    private final ProductDtoMapper mapper;

    public ProductCommandController(ReserveProductCommandHandler reserveHandler,
                                    ReleaseProductCommandHandler releaseProductHandler,
                                    ReleaseProductByCartCommandHandler releaseByCartHandler,
                                    GetProductQueryHandler queryHandler,
                                    ProductDtoMapper mapper) {
        this.reserveHandler = reserveHandler;
        this.releaseProductHandler = releaseProductHandler;
        this.releaseByCartHandler = releaseByCartHandler;
        this.queryHandler = queryHandler;
        this.mapper = mapper;
    }

    @Deprecated(forRemoval = true)
    @PostMapping("/{productId}/reserve")
    public ResponseEntity<ProductDto> reserveProduct(@PathVariable UUID productId,
                                                     @RequestParam UUID cartId,
                                                     @RequestParam int quantity) {
        reserveHandler.handle(new ReserveProductCommand(productId, cartId, quantity));
        ProductDto product = mapper.toProductDto(queryHandler.handle(new GetProductQuery(productId)));
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}/release")
    public ResponseEntity<ProductDto> releaseProductPartially(@PathVariable UUID productId,
                                                              @RequestParam UUID cartId,
                                                              @RequestParam int quantity) {
        releaseProductHandler.handle(new ReleaseProductCommand(productId, cartId, quantity));
        ProductDto product = mapper.toProductDto(queryHandler.handle(new GetProductQuery(productId)));
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/release-by-cart/{cartId}")
    public ResponseEntity<Void> releaseByCart(@PathVariable UUID cartId) {
        releaseByCartHandler.handle(new ReleaseProductByCartCommand(cartId));
        return ResponseEntity.noContent().build();
    }
}
