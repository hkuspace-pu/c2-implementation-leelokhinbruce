package com.example.guestreservationapp.reservation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.guestreservationapp.databinding.ActivityConfirmBookingBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.mainpage.ReservationsFragment;
import com.example.guestreservationapp.viewmodel.ReservationViewModel;
import com.example.restaurant_reservation_lib.entity.BookReservation;
import com.example.restaurant_reservation_lib.entity.Reservation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfirmBookingActivity extends AppCompatActivity {
    private ActivityConfirmBookingBinding binding;
    private ReservationViewModel viewModel;
    private BookReservation reservation;
    private Guest guest;
    private boolean isEditReservation, isConfinue;
    private ExecutorService executorService;
    private Handler mainHandler;

    private final String CONFIRM_BOOKING = "Confirm Booking",
                        EDIT_BOOKING = "Edit Booking",
                        BOOK_NOW_BUTTON = "Book Now",
                        UPDATE_BUTTON = "Update & Rebook";

    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String IS_CONTINUE = "IS_CONTINUE";
    public static final String EXTRA_DATE = "com.example.guestreservationapp.EXTRA_DATE";
    public static final String EXTRA_TIME = "com.example.guestreservationapp.EXTRA_TIME";
    public static final String EXTRA_GUEST = "com.example.guestreservationapp.EXTRA_GUEST";
    public static final String EXTRA_OCCASION = "com.example.guestreservationapp.EXTRA_OCCASION";
    public static final String EXTRA_OFFER = "com.example.guestreservationapp.EXTRA_OFFER";

    // Request code
    public static final int EDIT_RESERVATION_DETAILS = 1;
    public static final int EDIT_BOOKING_OFFER = 2;
    public static final int EDIT_GUEST_AND_DINNER_INFO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Init View Model
        viewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        isEditReservation = getIntent().getBooleanExtra(EDIT_MODE, false);
        isConfinue = getIntent().getBooleanExtra(IS_CONTINUE, false);

        // Set screen title & button text
        binding.textScreenTitle.setText(isEditReservation ? EDIT_BOOKING : CONFIRM_BOOKING);
        binding.btnBookNow.setText(isEditReservation ? UPDATE_BUTTON : BOOK_NOW_BUTTON);

        // IF Continue the booking or in Edit Reservation
        if (isConfinue || isEditReservation) {
            // Get data from previous Activity
            String time = getIntent().getStringExtra(EXTRA_TIME);
            int partySize = getIntent().getIntExtra(EXTRA_GUEST, 0);
            String date = getIntent().getStringExtra(EXTRA_DATE);
            String offer = getIntent().getStringExtra(EXTRA_OFFER);
            String occasion = getIntent().getStringExtra(EXTRA_OCCASION);

            // Initial setup
            BookReservation.init(new BookReservation.Builder(date, time, partySize, generateBookNo())
                    .setOccasion(occasion)
                    .setSpecialOffer(offer));
        }

        // Get singleton Reservation instance
        reservation = BookReservation.getInstance();
        guest = Guest.getInstance();

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
            startActivityForResult(intent, EDIT_RESERVATION_DETAILS);
        });

        // Select special offer
        binding.cardViewBtnOffer.setOnClickListener(viewSelectOffer -> {
            Intent intent = new Intent(this, BookingOfferActivity.class);
            intent.putExtra(EDIT_MODE, true);
            startActivityForResult(intent, EDIT_BOOKING_OFFER);
        });

        // Edit Dinner Info
        binding.textLinkEditGuestAndDinnerInfo.setOnClickListener(viewEditDinnerInfo -> {
            Intent intent = new Intent(this, GuestAndDinnerInfoActivity.class);
            startActivityForResult(intent, EDIT_GUEST_AND_DINNER_INFO);
        });

        // Book Now
        binding.btnBookNow.setOnClickListener(viewBookNow -> {
            String title = isEditReservation ? EDIT_BOOKING : CONFIRM_BOOKING;
            String msg = isEditReservation ? "Are you sure to update the reservation details?" : "Are you sure to book the reservation?";
            String positiveBtn = isEditReservation ? UPDATE_BUTTON : BOOK_NOW_BUTTON;

            new MaterialAlertDialogBuilder(ConfirmBookingActivity.this)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(positiveBtn, (dialog, i) ->
                            executorService.execute(() -> saveReservation(reservation, guest)))
                    .setNegativeButton("Cancel", (dialog, i) ->
                            dialog.cancel()).show();
        });
    }

    // Save reservation data into local DB + sync with server DB
    private void saveReservation(BookReservation bookReservation, Guest guest) {
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
                1,
                guest.getFirstName() + " " + guest.getLastName(),
                guest.getEmail(),
                guest.getPhoneNumber()
        );

        if (isEditReservation) {
            BookReservation.resetInstance();
            // Update the reservation obj into the local DB
            viewModel.updateReservation(reservation);
            mainHandler.post(() -> {
                Toast.makeText(this, "Updated reservation successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        } else {
            // Insert the reservation obj into the local DB
            viewModel.createReservation(reservation);
            mainHandler.post(() -> {
                Toast.makeText(this, "Booked reservation successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, BookSuccessActivity.class));
            });
        }
    }

    private String generateBookNo() {
        Random random = new Random();
        // Random upper letter (A-Z)
        char letter = (char) ('A' + random.nextInt(26));
        // Random 3-digit numbers (100-999)
        int number = 100 + random.nextInt(900);

        return letter + String.valueOf(number);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_RESERVATION_DETAILS) {
            binding.textTime.setText(reservation.getTime());
            binding.textGuest.setText(reservation.getGuestCount() + " guests");
            binding.textDate.setText(reservation.getDate());
            if (reservation.getOccasion() == null)
                binding.textOccasion.setText("Optional");
            else
                binding.textOccasion.setText(reservation.getOccasion());
        } else if (requestCode == EDIT_BOOKING_OFFER) {
            if (reservation.getSpecialOffer().equals("Without Offer"))
                binding.textSelectedOffer.setText("Please select an offer");
            else
                binding.textSelectedOffer.setText(reservation.getSpecialOffer());
        } else if (requestCode == EDIT_GUEST_AND_DINNER_INFO) {
            binding.textName.setText(guest.getFirstName() + " " + guest.getLastName());
            binding.textEmail.setText(guest.getEmail());
            binding.textPhoneNum.setText(guest.getPhoneNumber());
        }
    }
}