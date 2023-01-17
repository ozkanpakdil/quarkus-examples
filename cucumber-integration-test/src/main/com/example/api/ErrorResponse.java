package com.example.api;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {

    private Status status;
    private List<String> errors;

    public ErrorResponse(String errorMessage) {
        this.status = Status.error;
        this.errors = Arrays.asList(errorMessage);
    }
}
