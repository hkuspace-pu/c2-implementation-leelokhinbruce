package com.example.guestreservationapp.reservation;

import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_DATE;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_GUEST;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_OCCASION;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_TIME;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityBookingOfferBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingOfferActivity extends AppCompatActivity {
    private ActivityBookingOfferBinding binding;
    private MaterialCardView currentSelectedCard = null;
    private AppCompatImageButton checkedImgBtn = null;
    private String currentSelectedText = null;

    public static final String EXTRA_OFFER = "com.example.guestreservationapp.EXTRA_OFFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingOfferBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Set click listener for single selection
        binding.cardViewWithoutOffer.setOnClickListener(view ->
                selectCard(binding.cardViewWithoutOffer, binding.imgBtnWithoutOffer, binding.textWithoutOffer));
        binding.cardView15Percent.setOnClickListener(view ->
                selectCard(binding.cardView15Percent, binding.imgBtn15Percent, binding.text15Percent));
        binding.cardView30Percent.setOnClickListener(view ->
                selectCard(binding.cardView30Percent, binding.imgBtn30Percent, binding.text30Percent));

        boolean isEditMode = getIntent().getBooleanExtra(ConfirmBookingActivity.EDIT_MODE, false);
        if (isEditMode) {
            // Edit Mode
            binding.btnContinue.setText("Update");
            switch (Objects.requireNonNull(getIntent().getStringExtra(EXTRA_OFFER))) {
                case "Special Offer 15%":
                    selectCard(binding.cardView15Percent, binding.imgBtn15Percent, binding.text15Percent);
                    break;
                case "Special Offer 30%":
                    selectCard(binding.cardView30Percent, binding.imgBtn30Percent, binding.text30Percent);
                    break;
                default:
                    selectCard(binding.cardViewWithoutOffer, binding.imgBtnWithoutOffer, binding.textWithoutOffer);
            }
        } else {
            // Default selection
            selectCard(binding.cardViewWithoutOffer, binding.imgBtnWithoutOffer, binding.textWithoutOffer);
        }

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Continue button click
        binding.btnContinue.setOnClickListener(viewContinue -> {
            Intent data = new Intent(BookingOfferActivity.this, ConfirmBookingActivity.class);

            data.putExtra(EXTRA_DATE, getIntent().getStringExtra(EXTRA_DATE));
            data.putExtra(EXTRA_TIME, getIntent().getStringExtra(EXTRA_TIME));
            data.putExtra(EXTRA_GUEST, getIntent().getIntExtra(EXTRA_GUEST, 0));
            data.putExtra(EXTRA_OCCASION, getIntent().getStringExtra(EXTRA_OCCASION));
            data.putExtra(EXTRA_OFFER, currentSelectedText);

            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(data);
        });
    }

    // Selected card
    private void selectCard(MaterialCardView selectedCard, AppCompatImageButton imgBtn, AppCompatTextView textLayout) {
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
        currentSelectedText = textLayout.getText().toString();
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