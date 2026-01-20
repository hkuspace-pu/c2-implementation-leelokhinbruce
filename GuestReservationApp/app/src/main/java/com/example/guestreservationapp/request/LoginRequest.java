package com.example.guestreservationapp.request;

public class LoginRequest {
    private String usernameOrEmail, password;

    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
