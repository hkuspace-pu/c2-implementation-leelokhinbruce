package com.example.restaurant_reservation_lib.request;

public class ResetPasswordRequest {
    private String currentPasswd, newPasswd;

    public ResetPasswordRequest(String currentPasswd, String newPasswd) {
        this.currentPasswd = currentPasswd;
        this.newPasswd = newPasswd;
    }
}
