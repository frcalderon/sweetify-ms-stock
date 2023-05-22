package com.frcalderon.stock.controller.dto;

import com.frcalderon.stock.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponse {

    public StockResponse(Stock stock) {
        this.id = stock.getId();
        this.quantity = stock.getQuantity();
        this.expirationDate = stock.getExpirationDate();
        this.ingredientId = stock.getIngredientId();
        this.date = stock.getDate();
    }

    private Long id;

    private Double quantity;

    private LocalDate expirationDate;

    private Long ingredientId;

    private LocalDate date;
}
