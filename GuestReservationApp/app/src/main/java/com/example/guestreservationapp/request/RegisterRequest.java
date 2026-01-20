package com.example.guestreservationapp.request;

public class RegisterRequest {
    private String username, email, password, firstName, lastName, phoneNumber, gender;

    // Constructor
    public RegisterRequest(String username, String email, String password, String firstName, String lastName, String phoneNumber, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }
}
