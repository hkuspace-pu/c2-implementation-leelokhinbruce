package com.example.restaurant_reservation_lib.request;

public class LoginRequest {
    private String usernameOrEmail, password;

    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
