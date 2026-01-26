package com.example.restaurant_reservation_lib.request;

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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }
}
