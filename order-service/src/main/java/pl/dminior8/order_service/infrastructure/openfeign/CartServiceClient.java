package pl.dminior8.order_service.infrastructure.openfeign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.dminior8.common.dto.CartDto;


import java.util.UUID;

@FeignClient(name = "cart-service", url = "${external.cart-service.base-url}")
public interface CartServiceClient {

    @GetMapping("/api/v1/user/{userId}/cart/{cartId}")
    CartDto getCheckedOutCart(@PathVariable("userId") UUID userId, @PathVariable("cartId") UUID cartId);
}

