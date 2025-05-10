package pl.dminior8.product_service.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.product_service.domain.entity.Product;
import pl.dminior8.product_service.infrastructure.repository.ProductRepository;

import java.util.UUID;

@Service
public class ProductDomainService {

    private final ProductRepository repo;

    public ProductDomainService(ProductRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void decreaseStock(UUID prodId, int qty) {
        Product p = repo.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        p.decreaseQuantity(qty);
        repo.save(p);
    }

    @Transactional
    public void increaseStock(UUID prodId, int qty) {
        Product p = repo.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        p.increaseQuantity(qty);
        repo.save(p);
    }
}

