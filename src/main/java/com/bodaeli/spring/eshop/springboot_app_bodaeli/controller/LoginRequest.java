package com.bodaeli.spring.eshop.springboot_app_bodaeli.controller;

public class LoginRequest {
    private String username;
    private String password;

    // Constructor vacío (necesario para Spring)
    public LoginRequest() {}

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
