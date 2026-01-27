package com.example.adminreservationmanagementapp.accessing_data;

import com.example.adminreservationmanagementapp.Staff;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface StaffInfoApi {
    @GET("/api/staff/account_details")
    Call<Staff> getStaffData();

    @PATCH("/api/staff/account_details/edit")
    Call<String> updateAccountDetails(@Body Staff staff);
}
