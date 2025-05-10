package pl.dminior8.product_service.application.productReservation.command.releaseProduct;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dminior8.product_service.domain.service.ProductDomainService;
import pl.dminior8.product_service.domain.service.ReservationDomainService;

@Component
public class ReleaseProductCommandHandler {

    private final ProductDomainService productService;
    private final ReservationDomainService reservationService;

    public ReleaseProductCommandHandler(ProductDomainService productService, ReservationDomainService reservationService) {
        this.productService = productService;
        this.reservationService = reservationService;
    }

    @Transactional
    public void handle(ReleaseProductCommand cmd) {
        // 1. Odejmij rezerwację z tabeli
        reservationService.removeQuantityFromReservation(
                cmd.cartId(), cmd.productId(), cmd.quantity()
        );
        // 2. Przywróć stan produktu
        productService.increaseStock(cmd.productId(), cmd.quantity());
    }
}
