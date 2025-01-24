package com.example.dto;

import java.io.Serializable;

public class SuccessResponse implements Serializable {
    private String message;

    public SuccessResponse() {}

    public SuccessResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}