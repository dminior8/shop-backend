package pl.dminior8.cart_service.infrastructure.openfeign;

import org.springframework.web.bind.annotation.*;
import pl.dminior8.common.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.UUID;


@FeignClient(name = "product-service", url = "${external.product-service.base-url}")
public interface ExternalProductServiceClient {

    @GetMapping("/api/v1/products/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);

    @Deprecated(forRemoval = true)
    @PostMapping("/api/v1/products/{productId}/reserve")
    ProductDto reserveProduct(@PathVariable UUID productId, @RequestParam UUID userId, @RequestParam UUID cartId, @RequestParam int quantity);

    @Deprecated(forRemoval = true)
    @DeleteMapping("api/v1/products/{productId}/release")
    ProductDto releaseProductPartially(@PathVariable UUID productId, @RequestParam UUID userId, @RequestParam UUID cartId, @RequestParam int quantity);

    @DeleteMapping("api/v1/products/release-by-cart/{cartId}")
    ProductDto releaseByCart(@PathVariable UUID cartId);
}

