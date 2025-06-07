package pl.dminior8.product_service.api.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.product_service.application.common.mapper.ProductDtoMapper;
import pl.dminior8.product_service.application.product.query.getAllProducts.GetAllProductsQuery;
import pl.dminior8.product_service.application.product.query.getAllProducts.GetAllProductsQueryHandler;
import pl.dminior8.product_service.application.product.query.getProduct.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {
    private final GetProductQueryHandler getProductQueryHandler;
    private final GetAllProductsQueryHandler getAllProductsHandler;
    private final ProductDtoMapper mapper;

    public ProductQueryController(GetProductQueryHandler getProductQueryHandler, GetAllProductsQueryHandler getAllProductsHandler, ProductDtoMapper mapper) {
        this.getProductQueryHandler = getProductQueryHandler;
        this.getAllProductsHandler = getAllProductsHandler;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable("id") UUID id) {
        ProductDto dto = mapper.toProductDto(getProductQueryHandler.handle(new GetProductQuery(id)));
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

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = getAllProductsHandler.handle(new GetAllProductsQuery())
                .stream()
                .map(mapper::toProductDto)
                .toList();
        log.info("Received {} products.", productDtos.size());
        return ResponseEntity.ok(productDtos);
    }
}

