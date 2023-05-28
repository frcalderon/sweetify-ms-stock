package com.frcalderon.stock.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStockRequest {

    @NotBlank
    private Long ingredientId;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    private Double stock;
}
