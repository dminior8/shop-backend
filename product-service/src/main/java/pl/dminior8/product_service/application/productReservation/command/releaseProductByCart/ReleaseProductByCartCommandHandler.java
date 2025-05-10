package pl.dminior8.product_service.application.productReservation.command.releaseProductByCart;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.product_service.domain.service.ProductDomainService;
import pl.dminior8.product_service.domain.service.ReservationDomainService;
import pl.dminior8.product_service.domain.entity.ProductReservation;

@Component
public class ReleaseProductByCartCommandHandler {

    private final ReservationDomainService reservationService;
    private final ProductDomainService productService;

    public ReleaseProductByCartCommandHandler(ReservationDomainService reservationService,
                                              ProductDomainService productService) {
        this.reservationService = reservationService;
        this.productService = productService;
    }

    @Transactional
    public void handle(ReleaseProductByCartCommand cmd) {
        // 1. Pobierz wszystkie rezerwacje
        var reservations = reservationService.getReservationsByCart(cmd.cartId());
        // 2. Przywróć stan produktu dla każdej rezerwacji
        for (ProductReservation res : reservations) {
            productService.increaseStock(res.getProductId(), res.getQuantity());
        }
        // 3. Usuń rezerwacje
        reservationService.deleteAllByCart(cmd.cartId());
    }
}

