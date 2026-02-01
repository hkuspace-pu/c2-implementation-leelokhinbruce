package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityBookSuccessBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.restaurant_reservation_lib.entity.BookReservation;

public class BookSuccessActivity extends AppCompatActivity {
    private ActivityBookSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSuccessBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Get instance
        BookReservation reservation = BookReservation.getInstance();
        Guest guest = Guest.getInstance();
        // Set values to these layers
        binding.textName.setText(guest.getFirstName() + " " + guest.getLastName());
        binding.textEmail.setText(guest.getEmail());
        binding.textPhoneNum.setText(guest.getPhoneNumber());
        binding.textOccasion.setText(reservation.getOccasion());
        binding.textDate.setText(reservation.getDate());
        binding.textTime.setText(reservation.getTime());
        binding.textGuestNum.setText(reservation.getGuestCount() + " guest(s)");

        // View E-Ticket button click
        binding.btnViewTicket.setOnClickListener(viewTicket ->
                startActivity(new Intent(BookSuccessActivity.this, ETicketActivity.class)));

        // View Reservations button click
        binding.btnViewReservations.setOnClickListener(viewReservations -> {
            BookReservation.resetInstance();  // Clear reservation instance
            Intent intent = new Intent(BookSuccessActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities for booking reservation
            startActivity(intent);
        });
    }
}