package com.example.guestreservationapp.accessing_data;

import com.example.guestreservationapp.request.LoginRequest;
import com.example.guestreservationapp.request.RegisterRequest;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// API Service
public interface AuthApi {
    // Returns Void since backend returns String msg
    @POST("/api/auth/register")
    Call<Map<String, String>> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/auth/login")
    Call<Map<String, String>> loginUser(@Body LoginRequest loginRequest);
}
