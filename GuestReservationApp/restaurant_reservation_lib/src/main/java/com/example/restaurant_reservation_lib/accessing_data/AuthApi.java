package com.example.restaurant_reservation_lib.accessing_data;

import com.example.restaurant_reservation_lib.request.LoginRequest;
import com.example.restaurant_reservation_lib.request.RegisterRequest;

import java.util.Map;

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
