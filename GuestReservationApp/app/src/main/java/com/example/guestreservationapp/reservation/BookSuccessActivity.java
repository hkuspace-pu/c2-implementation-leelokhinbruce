package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityBookSuccessBinding;
import com.example.guestreservationapp.mainpage.MainActivity;

public class BookSuccessActivity extends AppCompatActivity {
    private ActivityBookSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSuccessBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.btnViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookSuccessActivity.this, ETicketActivity.class));
            }
        });

        binding.btnViewReservations.setOnClickListener(viewReservations -> {
            Intent intent = new Intent(BookSuccessActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}