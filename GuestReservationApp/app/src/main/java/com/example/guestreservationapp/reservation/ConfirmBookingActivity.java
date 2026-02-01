package com.example.guestreservationapp.reservation;

import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_DATE;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_GUEST;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_OCCASION;
import static com.example.guestreservationapp.reservation.ReservationActivity.EXTRA_TIME;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.guestreservationapp.databinding.ActivityConfirmBookingBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.viewmodel.ReservationViewModel;
import com.example.restaurant_reservation_lib.entity.BookReservation;
import com.example.restaurant_reservation_lib.entity.Reservation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfirmBookingActivity extends AppCompatActivity {
    private ReservationViewModel viewModel;
    private ExecutorService executorService;
    private Handler mainHandler;

    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String IS_CONTINUE = "IS_CONTINUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConfirmBookingBinding binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Init View Model
        viewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        // Is Continue (Not Edit)
        if (getIntent().getBooleanExtra(IS_CONTINUE, false)) {
            // Get data from previous Activity
            String time = getIntent().getStringExtra(EXTRA_TIME);
            int partySize = getIntent().getIntExtra(EXTRA_GUEST, 0);
            String date = getIntent().getStringExtra(EXTRA_DATE);
            String offer = getIntent().getStringExtra(BookingOfferActivity.EXTRA_OFFER);
            String occasion = getIntent().getStringExtra(EXTRA_OCCASION);

            // Initial setup
            BookReservation.init(new BookReservation.Builder(date, time, partySize, generateBookNo())
                    .setOccasion(occasion)
                    .setSpecialOffer(offer));
        }

        // Get singleton Reservation instance
        BookReservation reservation = BookReservation.getInstance();
        Guest guest = Guest.getInstance();

        // Set values in the current Activity
        binding.textTime.setText(reservation.getTime());
        binding.textGuest.setText(reservation.getGuestCount() + " guests");
        binding.textDate.setText(reservation.getDate());
        if (reservation.getSpecialOffer().equals("Without Offer"))
            binding.textSelectedOffer.setText("Please select an offer");
        else
            binding.textSelectedOffer.setText(reservation.getSpecialOffer());
        binding.textName.setText(guest.getFirstName() + " " + guest.getLastName());
        binding.textEmail.setText(guest.getEmail());
        binding.textPhoneNum.setText(guest.getPhoneNumber());
        if (reservation.getOccasion() == null)
            binding.textOccasion.setText("Optional");
        else
            binding.textOccasion.setText(reservation.getOccasion());

        // Cancel the book
        binding.imgBtnClose.setOnClickListener(viewClose -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Edit Reservation
        binding.btnEditReservationDetails.setOnClickListener(viewEditReservation -> {
            Intent intent = new Intent(this, ReservationActivity.class);
            intent.putExtra(EDIT_MODE, true);
            startActivity(intent);
        });

        // Select special offer
        binding.cardViewBtnOffer.setOnClickListener(viewSelectOffer -> {
            Intent intent = new Intent(this, BookingOfferActivity.class);
            intent.putExtra(EDIT_MODE, true);
            startActivity(intent);
        });

        // Edit Dinner Info
        binding.textLinkEditGuestAndDinnerInfo.setOnClickListener(viewEditDinnerInfo -> {
            Intent intent = new Intent(this, GuestAndDinnerInfoActivity.class);
            startActivity(intent);
        });

        // Book Now
        binding.btnBookNow.setOnClickListener(viewBookNow -> {
            new MaterialAlertDialogBuilder(ConfirmBookingActivity.this)
                    .setTitle("Book Now")
                    .setMessage("Are you sure to book the reservation?")
                    .setPositiveButton("Book", (dialog, i) ->
                            executorService.execute(() -> saveReservation(reservation)))
                    .setNegativeButton("Cancel", (dialog, i) ->
                            dialog.cancel()).show();
        });
    }

    // Save reservation data into local DB + sync with server DB
    private void saveReservation(BookReservation bookReservation) {
        // Build reservation obj
        Reservation reservation = new Reservation(
                bookReservation.getDate(),
                bookReservation.getTime(),
                "pending",
                bookReservation.getOccasion(),
                bookReservation.getSpecialOffer(),
                null,
                generateBookNo(),
                bookReservation.getGuestCount(),
                1
        );

        // Insert the reservation obj into the local DB
        viewModel.createReservation(reservation);

        mainHandler.post(() -> {
            Toast.makeText(this, "Booked reservation successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, BookSuccessActivity.class));
        });
    }

    private String generateBookNo() {
        Random random = new Random();
        // Random upper letter (A-Z)
        char letter = (char) ('A' + random.nextInt(26));
        // Random 3-digit numbers (100-999)
        int number = 100 + random.nextInt(900);

        return letter + String.valueOf(number);
    }
}