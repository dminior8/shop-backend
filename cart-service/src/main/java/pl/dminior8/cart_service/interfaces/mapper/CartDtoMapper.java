package pl.dminior8.cart_service.interfaces.mapper;

import org.mapstruct.Mapper;
import pl.dminior8.cart_service.domain.model.Cart;
import pl.dminior8.cart_service.interfaces.dto.CartDto;

@Mapper(componentModel = "spring")
public interface CartDtoMapper {
    CartDto toCartDto(Cart cart);
}

