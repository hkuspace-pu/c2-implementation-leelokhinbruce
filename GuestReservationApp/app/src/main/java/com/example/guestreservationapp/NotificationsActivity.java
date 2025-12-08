package com.example.guestreservationapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.databinding.ActivityNotificationsBinding;

public class NotificationsActivity extends AppCompatActivity {
    private ActivityNotificationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}