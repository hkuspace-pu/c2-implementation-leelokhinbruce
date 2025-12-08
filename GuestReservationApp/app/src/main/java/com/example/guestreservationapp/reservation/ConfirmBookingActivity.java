package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityConfirmBookingBinding;
import com.example.guestreservationapp.mainpage.MainActivity;

public class ConfirmBookingActivity extends AppCompatActivity {
    private ActivityConfirmBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnClose.setOnClickListener(viewClose -> {
            Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        binding.btnBookNow.setOnClickListener(viewBookNow ->
                startActivity(new Intent(ConfirmBookingActivity.this, BookSuccessActivity.class)));
    }
}