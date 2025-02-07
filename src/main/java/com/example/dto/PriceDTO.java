package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
    @NotNull
    public UUID id;
    public Double value;
    public Date date;
    public UUID productId;
    public UUID storeId;
}