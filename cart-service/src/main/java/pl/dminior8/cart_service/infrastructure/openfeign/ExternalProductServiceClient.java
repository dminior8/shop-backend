package pl.dminior8.cart_service.infrastructure.openfeign;

import pl.dminior8.common.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;


@FeignClient(name = "product-service", url = "${external.product-service.base-url}")
public interface ExternalProductServiceClient {

    @GetMapping("/api/v1/products/{id}")
    ProductDto getProductById(@PathVariable("id") UUID productId);
}

