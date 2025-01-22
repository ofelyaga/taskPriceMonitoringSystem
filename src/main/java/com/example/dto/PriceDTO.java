package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
    public UUID id;
    public Double value;
    public LocalDate date;
    public UUID productId;
    public UUID storeId;
}