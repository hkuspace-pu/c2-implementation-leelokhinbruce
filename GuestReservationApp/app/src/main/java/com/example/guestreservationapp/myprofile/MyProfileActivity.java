package com.example.guestreservationapp.myprofile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.example.guestreservationapp.LoginActivity;
import com.example.guestreservationapp.databinding.ActivityMyProfileBinding;
import com.example.restaurant_reservation_lib.DataStoreManager;
import com.example.restaurant_reservation_lib.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MyProfileActivity extends SessionManager {
    private ActivityMyProfileBinding binding;
    private RxDataStore<Preferences> dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Init DataStore
        dataStore = DataStoreManager.getInstance(this);

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
                        clearToken(dataStore);  // Clear DataStore (remove token)

                        Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.cancel()).show();
        });
    }
}