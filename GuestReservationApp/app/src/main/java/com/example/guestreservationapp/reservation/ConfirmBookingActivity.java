package com.example.guestreservationapp.reservation;

import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_DATE;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_GUEST;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_OCCASION;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_TIME;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityConfirmBookingBinding;
import com.example.guestreservationapp.mainpage.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfirmBookingActivity extends AppCompatActivity {
    private ActivityConfirmBookingBinding binding;
    private String time, date, offer, occasion;
    private int partySize;
    private ExecutorService executorService;
    private Handler mainHandler;

    public static final String EDIT_MODE = "EDIT_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Get data from previous Activity
        time = getIntent().getStringExtra(EXTRA_TIME);
        partySize = getIntent().getIntExtra(EXTRA_GUEST, 0);
        date = getIntent().getStringExtra(EXTRA_DATE);
        offer = getIntent().getStringExtra(BookingOfferActivity.EXTRA_OFFER);
        occasion = getIntent().getStringExtra(EXTRA_OCCASION);
        // Set values in the current Activity
        binding.textTime.setText(time);
        binding.textGuest.setText(String.valueOf(partySize));
        binding.textDate.setText(date);
        if (offer.equals("Without Offer"))
            binding.textSelectedOffer.setText("Please select an offer");
        else
            binding.textSelectedOffer.setText(offer);
        if (occasion == null)
            binding.textOccasion.setText("Optional");
        else
            binding.textOccasion.setText(occasion);

        // Cancel the book
        binding.imgBtnClose.setOnClickListener(viewClose -> {
            Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Edit Reservation
        binding.btnEditReservationDetails.setOnClickListener(viewEditReservation -> {
            Intent intent = new Intent(ConfirmBookingActivity.this, ReservationActivity.class);
            intent.putExtra(EDIT_MODE, true);

            // Pass data
            intent.putExtra(EXTRA_DATE, date);
            intent.putExtra(EXTRA_TIME, time);
            intent.putExtra(EXTRA_GUEST, partySize);
            intent.putExtra(EXTRA_OCCASION, occasion);
            intent.putExtra(BookingOfferActivity.EXTRA_OFFER, offer);

            startActivity(intent);
        });

        // Select special offer
        binding.cardViewBtnOffer.setOnClickListener(viewSelectOffer -> {
            Intent intent = new Intent(ConfirmBookingActivity.this, BookingOfferActivity.class);
            intent.putExtra(EDIT_MODE, true);

            // Pass data
            intent.putExtra(EXTRA_DATE, date);
            intent.putExtra(EXTRA_TIME, time);
            intent.putExtra(EXTRA_GUEST, partySize);
            intent.putExtra(EXTRA_OCCASION, occasion);
            intent.putExtra(BookingOfferActivity.EXTRA_OFFER, offer);

            startActivity(intent);
        });

        // Book Now
        binding.btnBookNow.setOnClickListener(viewBookNow ->
                startActivity(new Intent(ConfirmBookingActivity.this, BookSuccessActivity.class)));
    }
}