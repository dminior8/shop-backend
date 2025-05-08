package pl.dminior8.product_service.application.product.query.getProduct;


import org.springframework.stereotype.Component;
import pl.dminior8.product_service.domain.entity.Product;
import pl.dminior8.product_service.infrastructure.repository.ProductRepository;

@Component
public class GetProductQueryHandler {
    private final ProductRepository productRepository;

    public GetProductQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product handle(GetProductQuery query) {
        return productRepository.findById(query.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + query.productId()));
    }

}

