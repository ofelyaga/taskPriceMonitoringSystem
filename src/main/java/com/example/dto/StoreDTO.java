package com.example.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StoreDTO {
    private UUID id;
    private String name;
    private String city;
}