package pl.dminior8.product_service.infrastructure.messaging.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.common.event.ProductRemovedEvent;
import pl.dminior8.common.event.ProductReservedEvent;
import pl.dminior8.product_service.application.common.mapper.ProductDtoMapper;
import pl.dminior8.product_service.application.product.query.getProduct.GetProductQuery;
import pl.dminior8.product_service.application.product.query.getProduct.GetProductQueryHandler;
import pl.dminior8.product_service.application.productReservation.command.releaseProduct.ReleaseProductCommand;
import pl.dminior8.product_service.application.productReservation.command.releaseProduct.ReleaseProductCommandHandler;
import pl.dminior8.product_service.application.productReservation.command.reserveProduct.ReserveProductCommand;
import pl.dminior8.product_service.application.productReservation.command.reserveProduct.ReserveProductCommandHandler;

import java.util.UUID;

import static pl.dminior8.product_service.infrastructure.config.RabbitMqConfig.PRODUCT_REMOVED_QUEUE;
import static pl.dminior8.product_service.infrastructure.config.RabbitMqConfig.PRODUCT_RESERVED_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceDomainEventListener {
    private final ReserveProductCommandHandler reserveHandler;
    private final ReleaseProductCommandHandler releaseProductHandler;
    private final GetProductQueryHandler queryHandler;
    private final ProductDtoMapper mapper;

    @RabbitListener(queues = PRODUCT_RESERVED_QUEUE)
    public UUID handleProductReserved(ProductReservedEvent event) {
        log.info("[PRODUCT-SERVICE] Handling ProductReservedEvent for productId={}, quantity={}, cartId={}",
                event.productId(), event.quantity(), event.cartId());
        reserveHandler.handle(new ReserveProductCommand(event.productId(), event.cartId(), event.quantity()));
        ProductDto product = mapper.toProductDto(queryHandler.handle(new GetProductQuery(event.productId())));
        log.info("[PRODUCT-SERVICE] Reserved product={}, quantity={}, price={}",
                event.productId(), event.quantity(), product.getPrice());
        return event.cartItemId();
    }

    @RabbitListener(queues = PRODUCT_REMOVED_QUEUE)
    public void handleProductRemoved(ProductRemovedEvent event) {
        log.info("[PRODUCT-SERVICE] Handling ProductRemovedEvent for productId={}, quantity={}, cartId={}",
                event.productId(), event.quantity(), event.cartId());
        releaseProductHandler.handle(new ReleaseProductCommand(event.productId(), event.cartId(), event.quantity()));
        ProductDto product = mapper.toProductDto(queryHandler.handle(new GetProductQuery(event.productId())));
        log.info("[PRODUCT-SERVICE] Deleted product={}, quantity={}, price={}",
                event.productId(), event.quantity(), product.getPrice());
    }

}
