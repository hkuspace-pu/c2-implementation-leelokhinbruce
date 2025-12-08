package com.example.guestreservationapp.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.frameBtnNotificationSettings.setOnClickListener(viewNotificationSettings ->
                startActivity(new Intent(SettingsActivity.this, NotificationSettingsActivity.class)));

        binding.frameBtnResetPassword.setOnClickListener(viewResetPassword ->
                startActivity(new Intent(SettingsActivity.this, ResetPasswordActivity.class)));

        binding.frameBtnDeleteAccount.setOnClickListener(viewDeleteAccount ->
                startActivity(new Intent(SettingsActivity.this, DeleteAccountActivity.class)));

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}