package com.example.mesapp.unit_tests;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTestOne { //тестуємо чисті функції(прості математичні вирази)
    @Test
    public void test() {
        int firstNum = 4;
        int secondNum = 3;

        Assert.assertEquals(12, firstNum * secondNum);
        Assert.assertEquals(7, firstNum + secondNum);
        Assert.assertEquals(1, firstNum - secondNum);
    }
}
