package com.example.guestreservationapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.databinding.ActivityMyProfileBinding;

public class MyPointsActivity extends AppCompatActivity {
    private ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}