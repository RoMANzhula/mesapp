package com.example.mesapp.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
    static Map<String, String> getErrorsMapFromBindingResult(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap( //get Errors from BuildingResult
                fieldError -> fieldError.getField() + "Error", //key of Map
                FieldError::getDefaultMessage //value of Map
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
