package com.frcalderon.stock.controller.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
public class StockRequest {

    @NotNull
    @Digits(integer = 10, fraction = 2)
    private Double quantity;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String expirationDate;

    @NotNull
    private Long ingredientId;
}
