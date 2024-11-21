package com.example.springboot;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ApiException extends RuntimeException {
    private String message;
    private int status;
    private Map<String, String> errors;
}
