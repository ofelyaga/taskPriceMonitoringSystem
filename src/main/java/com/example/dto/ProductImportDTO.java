package com.example.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductImportDTO {
    private String name;
    private String manufacturer;
    private UUID categoryId;
}