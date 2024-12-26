package com.example.mesapp.unit_tests;

import com.example.mesapp.configurations.Encoders;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EncodersTest {
    @Test
    public void testOauthClientPasswordEncoder() {
        Encoders encoders = new Encoders();
        PasswordEncoder encoder = encoders.userPasswordEncoder();
        //встановлюємо наші очікувані значення та вхідні дані для тестування
        String rawPassword = "yourRawPassword";
        String encodedPassword = encoder.encode(rawPassword);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
        boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);

        assertEquals(true, matches); //перевіряємо, чи відбулася правильна кодування паролю

    }
}