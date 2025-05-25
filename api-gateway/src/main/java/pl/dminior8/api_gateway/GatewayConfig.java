package pl.dminior8.api_gateway;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cart-service", r -> r.path("/api/v1/user/**")
                        .uri("http://cart-service:8081"))
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .uri("http://product-service:8082"))
                .build();
    }
}


