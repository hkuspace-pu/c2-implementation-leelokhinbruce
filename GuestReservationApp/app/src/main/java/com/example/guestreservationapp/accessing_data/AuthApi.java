package com.example.guestreservationapp.accessing_data;

import com.example.guestreservationapp.request.LoginRequest;
import com.example.guestreservationapp.request.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// API Service
public interface AuthApi {
    // Returns Void since backend returns String msg
    @POST("/api/auth/register")
    Call<String> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/auth/login")
    Call<String> loginUser(@Body LoginRequest loginRequest);
}
