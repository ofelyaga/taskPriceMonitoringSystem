package com.example.exception;

import java.util.UUID;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(UUID categoryId) {
        super("Категория с ID " + categoryId + " не найдена");
    }
}