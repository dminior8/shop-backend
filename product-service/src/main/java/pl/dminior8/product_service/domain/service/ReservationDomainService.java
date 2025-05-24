package pl.dminior8.product_service.domain.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.product_service.domain.entity.ProductReservation;
import pl.dminior8.product_service.infrastructure.repository.ProductReservationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationDomainService {

    private final ProductReservationRepository productReservationRepository;

    public ReservationDomainService(ProductReservationRepository productReservationRepository) {
        this.productReservationRepository = productReservationRepository;
    }

    @Transactional
    public void addOrUpdateReservation(UUID cartId, UUID productId, int qty) {
        Optional<ProductReservation> productReservation = productReservationRepository.findByCartIdAndProductId(cartId, productId);
        if (productReservation.isPresent()) {
            productReservation.get().increaseQuantity(qty);
        } else {
            productReservationRepository.save(new ProductReservation(cartId, productId, qty));
        }
    }

    @Transactional
    public void removeQuantityFromReservation(UUID cartId, UUID prodId, int qty) {
        Optional<ProductReservation> r = productReservationRepository.findByCartIdAndProductId(cartId, prodId);
        if (r.isPresent()) {
            r.get().decreaseQuantity(qty);
            if (r.get().getQuantity() <= 0) {
                productReservationRepository.delete(r.get());
            }
        }
    }

    public List<ProductReservation> getReservationsByCart(UUID cartId) {
        return productReservationRepository.findAllByCartId(cartId);
    }

    public void deleteAllByCart(UUID cartId) {
        productReservationRepository.deleteAllByCartId(cartId);
    }
}

