package com.example.exception;

import java.util.UUID;

public class ProductAlreadyExistsException extends AppException {
    public ProductAlreadyExistsException(String name, String manufacturer, UUID categoryId) {
        super("Продукт с именем '" + name + "', производителем '" + manufacturer + "' и категорией ID " + categoryId + " уже существует");
    }
}