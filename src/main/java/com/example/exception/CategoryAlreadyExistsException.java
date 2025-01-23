package com.example.exception;

public class CategoryAlreadyExistsException extends AppException {
    public CategoryAlreadyExistsException(String name) {
        super("Категория с именем '" + name + "' уже существует");
    }
}