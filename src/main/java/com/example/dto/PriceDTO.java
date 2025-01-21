package com.example.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PriceDTO {
    public UUID id;
    public Double value;
    public LocalDate date;
    public UUID productId;
    public UUID storeId;
}