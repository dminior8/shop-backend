package pl.dminior8.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @Min(0)
    private int availableQuantity;

    @Min(0)
    private double price;
}
