package com.example.guestreservationapp.reservation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityEticketBinding;

public class ETicketActivity extends AppCompatActivity {
    private ActivityEticketBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEticketBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Back to Book Success screen
        binding.btnClose.setOnClickListener(viewClose -> finish());
    }
}