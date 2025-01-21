package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceComparisonDTO {
    public LocalDate date;
    public Double value;
    public String storeName; // Название магазина для сравнения
}