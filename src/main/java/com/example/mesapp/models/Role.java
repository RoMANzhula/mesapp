package com.example.mesapp.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    MANAGER;

    @Override
    public String getAuthority() {
        return name(); //повертаємо строкове уявлення об'єкта enum'a (USER, ADMIN, MANAGER)
    }
}
