package com.example.guestreservationapp.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}