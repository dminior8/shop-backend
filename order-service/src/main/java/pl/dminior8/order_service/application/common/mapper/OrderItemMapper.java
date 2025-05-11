package pl.dminior8.order_service.application.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dminior8.common.dto.CartItemDto;
import pl.dminior8.order_service.domain.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity",  target = "quantity")
    @Mapping(source = "price", target = "unitPrice")
    OrderItem toOrderItem(CartItemDto cartItem);
}
