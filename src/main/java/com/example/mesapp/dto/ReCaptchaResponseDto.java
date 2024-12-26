package com.example.mesapp.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true) //ігноруємо невідомі нам поля
public class ReCaptchaResponseDto {
    //в даному випадку відповідь API буде мати вигляд JSON з полями
    // "success": true|false,
    //  "challenge_ts": timestamp,  // timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
    //  "hostname": string,         // the hostname of the site where the reCAPTCHA was solved
    //  "error-codes": [...]        // optional
    //а ми залишаємо лише два поля
    private boolean success;
    @JsonAlias("error-codes") //аннотація яка виправляє код, в нашому випадку буде видаляти символ "-" так, як Java його не підтримує
    private Set<String> errorCodes;

    //геттери та сеттери
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}

