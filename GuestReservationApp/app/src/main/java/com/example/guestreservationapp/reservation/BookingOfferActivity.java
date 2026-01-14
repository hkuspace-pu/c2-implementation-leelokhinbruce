package com.example.guestreservationapp.reservation;

import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_DAY;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_GUEST;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_MONTH;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_OCCASION;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_TIME;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityBookingOfferBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingOfferActivity extends AppCompatActivity {
    private ActivityBookingOfferBinding binding;
    private MaterialCardView currentSelectedCard = null;
    private AppCompatImageButton checkedImgBtn = null;
    private String time, occasion;
    private int partySize, month, day;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingOfferBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Get data from previous Activity
        time = getIntent().getStringExtra(EXTRA_TIME);
        occasion = getIntent().getStringExtra(EXTRA_OCCASION);
        partySize = getIntent().getIntExtra(EXTRA_GUEST, 0);
        month = getIntent().getIntExtra(EXTRA_MONTH, 0);
        day = getIntent().getIntExtra(EXTRA_DAY, 0);

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Set click listener for single selection
        setupCardSelection();
        // Default selection
        selectCard(binding.cardViewWithoutOffer, binding.imgBtnWithoutOffer);

        // Continue button click
        binding.btnContinue.setOnClickListener(viewContinue -> {
            executorService.execute(() -> {
                Intent data = new Intent(BookingOfferActivity.this, ConfirmBookingActivity.class);
                data.putExtra(EXTRA_MONTH, month);
                data.putExtra(EXTRA_DAY, day);
                data.putExtra(EXTRA_TIME, time);
                data.putExtra(EXTRA_GUEST, partySize);
                data.putExtra(EXTRA_OCCASION, occasion);

                setResult(RESULT_OK, data);

                mainHandler.post(() -> startActivity(data));
            });
        });
    }

    private void setupCardSelection() {
        binding.cardViewWithoutOffer.setOnClickListener(view ->
                selectCard(binding.cardViewWithoutOffer, binding.imgBtnWithoutOffer));
        binding.cardView15Percent.setOnClickListener(view ->
                selectCard(binding.cardView15Percent, binding.imgBtn15Percent));
        binding.cardView30Percent.setOnClickListener(view ->
                selectCard(binding.cardView30Percent, binding.imgBtn30Percent));
    }

    private void selectCard(MaterialCardView selectedCard, AppCompatImageButton imgBtn) {
        // Deselect previous card
        if (currentSelectedCard != null && currentSelectedCard != selectedCard) {
            deselectedCard(currentSelectedCard, checkedImgBtn);
        }

        // Select new one
        // Change stroke color
        selectedCard.setStrokeColor(ColorStateList.valueOf(getResources().getColor(
                com.example.restaurant_reservation_lib.R.color.orange)));
        // Reset image
        imgBtn.setImageResource(com.example.restaurant_reservation_lib.R.drawable.check_icon);

        currentSelectedCard = selectedCard;
        checkedImgBtn = imgBtn;
    }

    // Unselected card
    private void deselectedCard(MaterialCardView cardView, AppCompatImageButton imgBtn) {
        // Change stroke color
        cardView.setStrokeColor(ColorStateList.valueOf(getResources().getColor(
                com.example.restaurant_reservation_lib.R.color.hint_and_stroke)));
        // Reset image
        imgBtn.setImageResource(com.example.restaurant_reservation_lib.R.drawable.circle);
    }
}