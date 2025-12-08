package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityReservationBinding;
import com.example.guestreservationapp.mainpage.MainActivity;

import java.util.Arrays;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    private ActivityReservationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Sample data to populate the cards
        List<Pair<String, String>> datesAndLabels = Arrays.asList(
                new Pair<>("Nov 6", "Today"),
                new Pair<>("Nov 7", "Tomorrow"),
                new Pair<>("Nov 8", "Sun"),
                new Pair<>("Nov 9", "Mon")
        );
        List<String> times = Arrays.asList("16:00", "16:30", "17:00", "17:30", "18:00", "18:30");
        List<Integer> guests = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<String> occasions = Arrays.asList("Birthday", "Friends Gathering", "Family Gathering", "Celebration", "Dating");

        // Loop through the data and inflate card views
        for (Pair<String, String> item : datesAndLabels) {
            // Inflate the card_item
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.date_cardview,
                    binding.dateCardContainer, false);

            // Reference in the date_cardview.xml
            AppCompatTextView dateText = cardView.findViewById(R.id.textDate);
            AppCompatTextView labelText = cardView.findViewById(R.id.textLabel);

            // Set values
            dateText.setText(item.first);
            labelText.setText(item.second);

            // Add card view to the container
            binding.dateCardContainer.addView(cardView);
        }

        // Loop through the time and inflate card views
        for (String time : times) {
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.time_cardview,
                    binding.dateCardContainer, false);

            // Reference in the time_cardview.xml
            AppCompatTextView timeText = cardView.findViewById(R.id.textTime);

            // Set value
            timeText.setText(time);

            binding.timeCardContainer.addView(cardView);
        }

        // Loop through the guest and inflate card views
        for (Integer guest : guests) {
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.guest_cardview,
                    binding.guestCardContainer, false);

            // Reference in the time_cardview.xml
            AppCompatTextView guestText = cardView.findViewById(R.id.textGuest);

            // Set value
            guestText.setText(guest);

            binding.guestCardContainer.addView(cardView);
        }

        // Loop through the guest and inflate card views
        for (String occasion : occasions) {
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.occasion_cardview,
                    binding.occasionCardContainer, false);

            // Reference in the time_cardview.xml
            AppCompatTextView occasionText = cardView.findViewById(R.id.textOccasion);

            // Set value
            occasionText.setText(occasion);

            binding.occasionCardContainer.addView(cardView);
        }

        // Close Editing Form and back to mainpage
        binding.imgBtnClose.setOnClickListener(viewClose -> {
            Intent intent = new Intent(ReservationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}