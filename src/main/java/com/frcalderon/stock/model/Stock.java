package com.frcalderon.stock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private Double quantity;

    @Column
    @NotNull
    private LocalDate expirationDate;

    @Column
    @NotNull
    private Long ingredientId;

    @Column
    @NotNull
    private LocalDate date;
}
