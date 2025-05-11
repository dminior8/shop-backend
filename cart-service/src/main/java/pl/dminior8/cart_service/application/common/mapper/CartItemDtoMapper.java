package pl.dminior8.cart_service.application.common.mapper;


import org.mapstruct.Mapper;
import pl.dminior8.cart_service.domain.entity.CartItem;
import pl.dminior8.common.dto.CartItemDto;

@Mapper(componentModel = "spring")
public interface CartItemDtoMapper {
    CartItemDto toCartItemDto(CartItem cartItem);
}

