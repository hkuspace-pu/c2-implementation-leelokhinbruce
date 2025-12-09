package com.example.adminreservationmanagementapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.ActivityLoginBinding;
import com.example.adminreservationmanagementapp.databinding.ActivitySettingsBinding;

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
    }
}