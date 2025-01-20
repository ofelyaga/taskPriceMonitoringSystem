package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceComparisonDTO {
    private LocalDate date;
    private Double value;
    private String storeName; // Название магазина для сравнения
}