package com.example.adminreservationmanagementapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adminreservationmanagementapp.LoginActivity;
import com.example.adminreservationmanagementapp.Staff;
import com.example.adminreservationmanagementapp.databinding.ActivitySettingsBinding;
import com.example.restaurant_reservation_lib.session_management.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        binding.frameBtnNotificationSettings.setOnClickListener(viewNotificationSettings ->
                startActivity(new Intent(SettingsActivity.this, NotificationSettingsActivity.class)));

        binding.frameBtnChangeBranchStatus.setOnClickListener(viewChangeBranchStatus ->
                startActivity(new Intent(SettingsActivity.this, ChangeBranchStatusActivity.class)));

        // Logout button click
        binding.btnLogout.setOnClickListener(viewLogout ->
                new MaterialAlertDialogBuilder(SettingsActivity.this)
                .setTitle("Logout Account")
                .setMessage("Are you sure want to sign out your account?")
                .setPositiveButton("Sign Out", (dialog, which) -> {
                    Toast.makeText(SettingsActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    new SessionManager(getApplicationContext()).clearToken();  // Clear DataStore (remove token)
                    Staff.resetInstance();  // Clear guest instance

                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.cancel()).show());
    }
}