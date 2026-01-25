package com.example.guestreservationapp.accessing_data;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.request.LoginRequest;
import com.example.guestreservationapp.request.RegisterRequest;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

// API Service
public interface AuthApi {
    // Returns Void since backend returns String msg
    @POST("/api/auth/register")
    Call<Map<String, String>> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/auth/login")
    Call<Map<String, String>> loginUser(@Body LoginRequest loginRequest);

    // Token in the Authorization header as "Bearer <token>" to verify the user
    @GET("/api/guest/guest_info")
    Call<Guest> getGuestData(@Header("Authorization") String token);
}
