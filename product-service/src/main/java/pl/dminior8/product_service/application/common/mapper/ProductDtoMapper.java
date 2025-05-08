package pl.dminior8.product_service.application.common.mapper;


import org.mapstruct.Mapper;
import pl.dminior8.common.dto.ProductDto;
import pl.dminior8.product_service.domain.entity.Product;


@Mapper(componentModel = "spring")
public class ProductDtoMapper {

    public ProductDto toProductDto(Product p) {
        if (p == null) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        if (p.getId() != null) {
            productDto.setId(p.getId());
        }
        if (p.getName() != null) {
            productDto.setName(p.getName());
        }
        if (p.getAvailableQuantity() >= 0) {
            productDto.setAvailableQuantity(p.getAvailableQuantity());
        }
        if (p.getPrice() > 0) {
            productDto.setPrice(p.getPrice());
        }
        return productDto;
    }
}
