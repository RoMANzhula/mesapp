package com.example.mesapp.exeptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        // Обробка помилок тут
        return "error"; // Повернення сторінки з повідомленням про помилку
    }
}
