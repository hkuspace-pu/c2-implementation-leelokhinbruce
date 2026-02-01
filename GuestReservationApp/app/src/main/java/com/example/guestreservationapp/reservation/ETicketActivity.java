package com.example.guestreservationapp.reservation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityEticketBinding;
import com.example.restaurant_reservation_lib.entity.BookReservation;

public class ETicketActivity extends AppCompatActivity {
    private ActivityEticketBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEticketBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Get instance
        BookReservation reservation = BookReservation.getInstance();
        Guest guest = Guest.getInstance();
        // Set values to these layers
        binding.textBookingNo.setText(reservation.getBookingNo());
        binding.textTime.setText(reservation.getTime());
        binding.textGuest.setText(reservation.getGuestCount() + " guest(s)");
        binding.textDate.setText(reservation.getDate());
        binding.textName.setText(guest.getFirstName() + " " + guest.getLastName());
        binding.textEmail.setText(guest.getEmail());
        binding.textPhoneNum.setText(guest.getPhoneNumber());
        binding.textOccasion.setText(reservation.getOccasion());

        // Back to Book Success screen
        binding.btnClose.setOnClickListener(viewClose -> finish());
    }
}