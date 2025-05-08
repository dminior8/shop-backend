package pl.dminior8.product_service.api.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.product_service.application.common.mapper.ProductDtoMapper;
import pl.dminior8.product_service.application.product.query.getProduct.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {
    private final GetProductQueryHandler handler;
    private final ProductDtoMapper mapper;

    public ProductQueryController(GetProductQueryHandler handler, ProductDtoMapper mapper) {
        this.handler = handler;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable("id") UUID id) {
        ProductDto dto = mapper.toProductDto(handler.handle(new GetProductQuery(id)));
        log.info("Received product details:\n" +
                        "ID: {}\n" +
                        "Name: {}\n" +
                        "Available Quantity: {}\n" +
                        "Price: {} PLN",
                dto.getId(),
                dto.getName(),
                dto.getAvailableQuantity(),
                dto.getPrice());
        return ResponseEntity.ok(dto);
    }
}

