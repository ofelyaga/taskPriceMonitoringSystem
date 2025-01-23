package com.example.exception;

import java.util.UUID;

public class ProductNotFoundException extends AppException {
    public ProductNotFoundException(UUID productId) {
        super("Продукт с ID " + productId + " не найден");
    }
}