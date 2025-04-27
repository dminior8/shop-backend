package pl.dminior8.cart_service.interfaces.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dminior8.cart.domain.model.Cart;
import pl.dminior8.common.dto.CartDto;

@Mapper(componentModel = "spring")
public interface CartDtoMapper {

    @Mapping(source = "id", target = "cartId")
    CartDto toDto(Cart cart);
}

