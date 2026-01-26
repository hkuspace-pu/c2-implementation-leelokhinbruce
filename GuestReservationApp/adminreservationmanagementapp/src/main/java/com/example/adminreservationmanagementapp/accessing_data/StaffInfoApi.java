package com.example.adminreservationmanagementapp.accessing_data;

import com.example.adminreservationmanagementapp.Staff;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StaffInfoApi {
    @GET("/api/staff/account_details")
    Call<Staff> getStaffData();
}
