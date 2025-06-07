package pl.dminior8.product_service.application.product.query.getAllProducts;


import org.springframework.stereotype.Component;
import pl.dminior8.product_service.domain.entity.Product;
import pl.dminior8.product_service.infrastructure.repository.ProductRepository;

import java.util.List;

@Component
public class GetAllProductsQueryHandler {
    private final ProductRepository productRepository;

    public GetAllProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> handle(GetAllProductsQuery query) {
        return productRepository.findAll();
    }
}
