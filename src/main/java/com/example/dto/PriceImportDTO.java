package com.example.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PriceImportDTO {
    private Double value;
    private LocalDate date;
    private UUID productId;
    private UUID storeId;
}