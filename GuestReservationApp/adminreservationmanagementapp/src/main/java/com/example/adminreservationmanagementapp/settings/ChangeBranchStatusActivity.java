package com.example.adminreservationmanagementapp.settings;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.ActivityChangeBranchStatusBinding;

public class ChangeBranchStatusActivity extends AppCompatActivity {
    private ActivityChangeBranchStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeBranchStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}