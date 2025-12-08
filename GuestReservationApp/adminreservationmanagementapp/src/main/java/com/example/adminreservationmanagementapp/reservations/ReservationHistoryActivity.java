package com.example.adminreservationmanagementapp.reservations;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.adminreservationmanagementapp.databinding.ActivityReservationHistoryBinding;

public class ReservationHistoryActivity extends AppCompatActivity {
    private ActivityReservationHistoryBinding binding;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationHistoryBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}
