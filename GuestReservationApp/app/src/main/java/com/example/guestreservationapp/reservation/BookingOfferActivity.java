package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityBookingOfferBinding;

public class BookingOfferActivity extends AppCompatActivity {
    private ActivityBookingOfferBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingOfferBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        binding.btnContinue.setOnClickListener(viewContinue ->
                startActivity(new Intent(BookingOfferActivity.this, ConfirmBookingActivity.class)));
    }
}