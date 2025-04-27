package pl.dminior8.cart_service.infrastructure.external;

import pl.dminior8.common.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client do komunikacji z product-service.
 */
@FeignClient(name = "product-service", url = "${external.product-service.base-url}")
public interface ProductServiceClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable("id") String productId);
}

