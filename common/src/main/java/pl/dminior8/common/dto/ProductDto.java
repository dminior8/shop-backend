package pl.dminior8.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank
    private UUID id;

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private int availableQuantity;

    @Min(0)
    private double price;
}
