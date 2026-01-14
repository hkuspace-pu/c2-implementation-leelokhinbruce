package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityReservationBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationActivity extends AppCompatActivity {
    private ActivityReservationBinding binding;
    private String time, occasion;
    private int partySize, month, day;
    private ExecutorService executorService;
    private Handler mainHandler;

    public static final String EXTRA_MONTH = "com.example.guestreservationapp.EXTRA_MONTH";
    public static final String EXTRA_DAY = "com.example.guestreservationapp.EXTRA_DAY";
    public static final String EXTRA_TIME = "com.example.guestreservationapp.EXTRA_TIME";
    public static final String EXTRA_GUEST = "com.example.guestreservationapp.EXTRA_GUEST";
    public static final String EXTRA_OCCASION = "com.example.guestreservationapp.EXTRA_OCCASION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Close Editing Form and back to main page
        binding.imgBtnClose.setOnClickListener(viewClose -> {
            finish();
        });

        // Get today's date
        Calendar today = Calendar.getInstance();
        // Initialize DatePicker with the current date
        binding.datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                // Monitor DatePicker date changes
                (datePicker, year, month, day) -> {
                    // Get date data
                    this.month = month;
                    this.day = day;
                }
        );
        // Restrict to future dates
        binding.datePicker.setMinDate(today.getTimeInMillis());
        // Hide the year spinner
        Resources resources = getResources();
        int yearId = resources.getIdentifier("year", "id", "android");
        NumberPicker yearPicker = binding.datePicker.findViewById(yearId);
        if (yearPicker != null)
            yearPicker.setVisibility(View.GONE);

        // Initialize array list
        ArrayList<String> timeList = new ArrayList<>(Arrays.asList(
                "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
                "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00"
                ));
        ArrayList<String> guestList = new ArrayList<>(Arrays.asList(
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        ));
        ArrayList<String> occasionList = new ArrayList<>(Arrays.asList(
                "Birthday", "Friends Gathering", "Family Gathering", "Dating", "Celebration"
        ));

        // Initialize chips in each chip group
        setChipGroup(timeList, binding.chipGroupTime);
        setChipGroup(guestList, binding.chipGroupGuest);
        setChipGroup(occasionList, binding.chipGroupOccasion);

        // Get a selected chip from Time field
        binding.chipGroupTime.setOnCheckedChangeListener((chipGroup, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = findViewById(checkedId);
                time = chip.getText().toString();
            }
        });
        // Get a selected chip from Time field
        binding.chipGroupGuest.setOnCheckedChangeListener((chipGroup, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = findViewById(checkedId);
                partySize = Integer.parseInt(chip.getText().toString());
            }
        });
        // Get a selected chip from Time field
        binding.chipGroupOccasion.setOnCheckedChangeListener((chipGroup, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = findViewById(checkedId);
                occasion = chip.getText().toString();
            }
        });

        // Continue button click
        binding.btnContinue.setOnClickListener(viewContinue -> {
            executorService.execute(() -> {
                Intent data = new Intent(ReservationActivity.this, BookingOfferActivity.class);
                data.putExtra(EXTRA_MONTH, month);
                data.putExtra(EXTRA_DAY, day);
                data.putExtra(EXTRA_TIME, time);
                data.putExtra(EXTRA_GUEST, partySize);
                data.putExtra(EXTRA_OCCASION, occasion);

                setResult(RESULT_OK, data);
                Log.d("Continue button click", String.format("Pass date: month-%d day-%d", month, day));

                mainHandler.post(() -> startActivity(data));
            });
        });
    }

    private void setChipGroup(ArrayList<String> arrayList, ChipGroup chipGroup) {
        Chip firstChip = null;

        for (int i=0; i < arrayList.size(); i++) {
            String chipText = arrayList.get(i);

            // Inflate chip from layout
            Chip chip = (Chip) LayoutInflater.from(ReservationActivity.this).inflate(
                    R.layout.chip_layout, chipGroup, false);
            chip.setText(chipText);
            chip.setCheckable(true);
            chipGroup.addView(chip);  // Add to the chip group

            // Mark the first chip id
            if (i == 0 && chipGroup != binding.chipGroupOccasion)
                firstChip = chip;
        }

        // checked the first chip on each chip group by default
        if (firstChip != null)
            chipGroup.check(firstChip.getId());
    }
}