package com.example.mesapp.unit_tests;


import org.junit.Test;

public class ExceptionsTest {

    //для теста очікуємо саме цей ексепшн, інакше тест не виконається
    @Test(expected = ArithmeticException.class)
    public void error() {
        int numZero = 0;
        int result = 5 / numZero;
    }
}
