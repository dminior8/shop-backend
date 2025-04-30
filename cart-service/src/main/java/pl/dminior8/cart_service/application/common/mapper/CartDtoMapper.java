package pl.dminior8.cart_service.application.common.mapper;

import org.mapstruct.Mapper;
import pl.dminior8.cart_service.domain.entity.Cart;
import pl.dminior8.cart_service.application.common.dto.CartDto;

@Mapper(componentModel = "spring")
public interface CartDtoMapper {
    CartDto toCartDto(Cart cart);
}

