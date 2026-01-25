package com.example.guestreservationapp.myprofile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.LoginActivity;
import com.example.guestreservationapp.accessing_data.AuthApi;
import com.example.guestreservationapp.databinding.ActivityMyProfileBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends SessionManager {
    private ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Get guest instance
        Guest guest = Guest.getInstance();
        // Set user data to layouts
        String fullName = String.format("%s %s", guest.getFirstName(), guest.getLastName());
        binding.textName.setText(fullName);
        binding.textEmail.setText(guest.getEmail());
        binding.textPhoneNum.setText(guest.getPhoneNumber());

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Personal Detail button click
        binding.frameBtnPersonalDetails.setOnClickListener(viewPersonalDetails ->
                startActivity(new Intent(MyProfileActivity.this, PersonalDetailsActivity.class)));

        // Phone Number button click
        binding.frameBtnPhoneNumber.setOnClickListener(viewPhoneNum ->
                startActivity(new Intent(MyProfileActivity.this, PhoneNumberActivity.class)));

        // Logout button click
        binding.btnLogout.setOnClickListener(viewLogout -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Logout Account")
                    .setMessage("Are you sure want to sign out your account?")
                    .setPositiveButton("Sign Out", (dialog, which) -> {
                        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                        clearToken();  // Clear DataStore (remove token)
                        Guest.resetInstance();  // Clear guest instance

                        Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.cancel()).show();
        });
    }
}