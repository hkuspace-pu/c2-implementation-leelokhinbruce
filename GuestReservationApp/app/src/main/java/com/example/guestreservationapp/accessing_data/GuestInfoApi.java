package com.example.guestreservationapp.accessing_data;

import com.example.guestreservationapp.Guest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;

public interface GuestInfoApi {
    // Token in the Authorization header as "Bearer <token>" to verify the user
    @GET("/api/guest/profile_details")
    Call<Guest> getGuestData();

    @PATCH("/api/guest/profile_details/edit")
    Call<String> updateProfileDetails(@Body Guest guest);
}
