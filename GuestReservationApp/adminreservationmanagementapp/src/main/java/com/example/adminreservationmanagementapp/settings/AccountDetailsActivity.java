package com.example.adminreservationmanagementapp.settings;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.ActivityAccountDetailsBinding;

public class AccountDetailsActivity extends AppCompatActivity {
    private ActivityAccountDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnClose.setOnClickListener(viewClose -> finish());
        binding.btnClose.setOnClickListener(viewCancel -> finish());
    }
}