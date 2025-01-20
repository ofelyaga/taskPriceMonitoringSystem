package com.example.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;
    private String name;
    private String manufacturer;
    private UUID categoryId;
}