package pl.dminior8.cart_service.application.common.mapper;

import org.mapstruct.Mapper;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.common.dto.CartDto;

@Mapper(componentModel = "spring", uses = CartItemDtoMapper.class)
public interface CartDtoMapper {
    CartDto toCartDto(Cart cart);
}

