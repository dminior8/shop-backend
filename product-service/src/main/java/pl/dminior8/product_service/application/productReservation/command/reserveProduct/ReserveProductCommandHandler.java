package pl.dminior8.product_service.application.productReservation.command.reserveProduct;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.common.exceptions.product.ProductNotAvailableException;
import pl.dminior8.product_service.domain.entity.Product;
import pl.dminior8.product_service.domain.service.ReservationDomainService;
import pl.dminior8.product_service.infrastructure.repository.ProductRepository;

@Component
public class ReserveProductCommandHandler {

    private final ProductRepository productRepository;
    private final ReservationDomainService reservationDomainService;

    public ReserveProductCommandHandler(ProductRepository productRepository, ReservationDomainService reservationDomainService) {
        this.productRepository = productRepository;
        this.reservationDomainService = reservationDomainService;
    }

    @Transactional
    public void handle(ReserveProductCommand cmd) {
        Product p = productRepository.findByIdForUpdate(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (p.getAvailableQuantity() < cmd.quantity()) {
            throw new ProductNotAvailableException(cmd.productId(), cmd.quantity(), p.getAvailableQuantity());
        }
        p.decreaseQuantity(cmd.quantity());
        reservationDomainService.addOrUpdateReservation(cmd.cartId(), cmd.productId(), cmd.quantity());
        productRepository.save(p);
    }
}

