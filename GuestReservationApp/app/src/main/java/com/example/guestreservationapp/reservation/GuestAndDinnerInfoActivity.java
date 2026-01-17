package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityGuestAndDinnerInfoBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;

public class GuestAndDinnerInfoActivity extends AppCompatActivity {
    private ActivityGuestAndDinnerInfoBinding binding;
    private String name, email, phone, occasion;

    public static final String EXTRA_NAME = "com.example.guestreservationapp.EXTRA_NAME";
    public static final String EXTRA_EMAIL = "com.example.guestreservationapp.EXTRA_EMAIL";
    public static final String EXTRA_PHONE_NUMBER = "com.example.guestreservationapp.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_OCCASION = "com.example.guestreservationapp.EXTRA_OCCASION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestAndDinnerInfoBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());

        // Get date from ConfirmBookingActivity
        name = getIntent().getStringExtra(EXTRA_NAME);
        email = getIntent().getStringExtra(EXTRA_EMAIL);
        phone = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
        occasion = getIntent().getStringExtra(EXTRA_OCCASION);

        // Close Editing Form and back to main page
        binding.imgBtnBack.setOnClickListener(viewClose -> {
            finish();
        });

        ArrayList<String> occasionList = new ArrayList<>(Arrays.asList(
                "Birthday", "Friends Gathering", "Family Gathering", "Dating", "Celebration"
        ));

        // Add chips into the Occasion Chip Group
        for (int i=0; i<occasionList.size(); i++) {
            String chipText = occasionList.get(i);

            // Inflate chip from layout
            Chip chip = (Chip) LayoutInflater.from(GuestAndDinnerInfoActivity.this).inflate(
                    R.layout.chip_layout, binding.chipGroupOccasion, false);
            chip.setText(chipText);
            chip.setCheckable(true);
            binding.chipGroupOccasion.addView(chip);  // Add to the chip group
        }

        // Get a selected chip from Time field
        binding.chipGroupOccasion.setOnCheckedStateChangeListener(
                (chipGroup, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                occasion = null;
            } else {
                int checkedId = checkedIds.get(0);  // since single selection
                Chip chip = chipGroup.findViewById(checkedId);
                if (chip != null) {
                    occasion = chip.getText().toString();
                }
            }
        });

        // Update button click
        binding.btnUpdate.setOnClickListener(viewUpdate -> {
            Intent intent = new Intent(GuestAndDinnerInfoActivity.this, ConfirmBookingActivity.class);

            intent.putExtra(EXTRA_NAME, name);
            intent.putExtra(EXTRA_EMAIL, email);
            intent.putExtra(EXTRA_PHONE_NUMBER, phone);
            intent.putExtra(EXTRA_OCCASION, occasion);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}