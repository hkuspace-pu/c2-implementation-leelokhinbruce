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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationActivity extends AppCompatActivity {
    private ActivityReservationBinding binding;
    private String time = "8:00", occasion, formattedDate, offer;
    private int partySize = 1;
    private boolean isEditMode;

    public static final String EXTRA_DATE = "com.example.guestreservationapp.EXTRA_DATE";
    public static final String EXTRA_TIME = "com.example.guestreservationapp.EXTRA_TIME";
    public static final String EXTRA_GUEST = "com.example.guestreservationapp.EXTRA_GUEST";
    public static final String EXTRA_OCCASION = "com.example.guestreservationapp.EXTRA_OCCASION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Close Editing Form and back to main page
        binding.imgBtnClose.setOnClickListener(viewClose -> {
            finish();
        });

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

        // Initialize Date Picker
        setupDatePicker();

        isEditMode = getIntent().getBooleanExtra(ConfirmBookingActivity.EDIT_MODE, false);
        if (isEditMode) {
            // Get data from ConfirmBookingActivity
            formattedDate = getIntent().getStringExtra(EXTRA_DATE);
            time = getIntent().getStringExtra(EXTRA_TIME);
            partySize = getIntent().getIntExtra(EXTRA_GUEST, 0);
            occasion = getIntent().getStringExtra(EXTRA_OCCASION);
            offer = getIntent().getStringExtra(BookingOfferActivity.EXTRA_OFFER);

            // Set Date Picker
//            setSelectedDatePicker(formattedDate);

            // Set up selected chip
            setUpSelectedChip(binding.chipGroupTime, time);
            setUpSelectedChip(binding.chipGroupGuest, String.valueOf(partySize));
            setUpSelectedChip(binding.chipGroupOccasion, occasion);

            binding.btnContinue.setText("Update");
        } else {
            LocalDate today = LocalDate.now();
            updateFormattedDate(today);  // Set today by default
        }

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
//        binding.chipGroupOccasion.setOnCheckedChangeListener((chipGroup, checkedId) -> {
//            if (checkedId != View.NO_ID) {
//                Chip chip = findViewById(checkedId);
//                occasion = chip.getText().toString();
//            }
//        });
        binding.chipGroupOccasion.setOnCheckedStateChangeListener((chipGroup, checkedIds) -> {
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

        // Continue button click
        binding.btnContinue.setOnClickListener(viewContinue -> {
            if (isEditMode) {
                Intent data = new Intent(ReservationActivity.this, ConfirmBookingActivity.class);
                data.putExtra(BookingOfferActivity.EXTRA_OFFER, offer);
                data.putExtra(EXTRA_DATE, formattedDate);
                data.putExtra(EXTRA_TIME, time);
                data.putExtra(EXTRA_GUEST, partySize);
                data.putExtra(EXTRA_OCCASION, occasion);

                data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(data);
            } else {
                Intent data = new Intent(ReservationActivity.this, BookingOfferActivity.class);
                data.putExtra(EXTRA_DATE, formattedDate);
                data.putExtra(EXTRA_TIME, time);
                data.putExtra(EXTRA_GUEST, partySize);
                data.putExtra(EXTRA_OCCASION, occasion);

                startActivity(data);
            }
        });
    }

    // DatePicker
    private void setupDatePicker() {
        Calendar cal = Calendar.getInstance();
        // Initialize DatePicker with the current date
        binding.datePicker.init(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                // Monitor DatePicker date changes
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    // monthOfYear is 0-based -> add 1
                    LocalDate selectedDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
                    updateFormattedDate(selectedDate);
                }
        );
        // Restrict to future dates
        binding.datePicker.setMinDate(cal.getTimeInMillis());
        // Hide the year spinner
        Resources resources = getResources();
        int yearId = resources.getIdentifier("year", "id", "android");
        NumberPicker yearPicker = binding.datePicker.findViewById(yearId);
        if (yearPicker != null)
            yearPicker.setVisibility(View.GONE);
    }

    // Change date
    private void updateFormattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("MMM d EEE", Locale.ENGLISH);
        formattedDate = date.format(formatter);
    }

    // Add chips into a Chip Group
    private void setChipGroup(ArrayList<String> arrayList, ChipGroup chipGroup) {
        Chip firstChip = null;

        for (int i = 0; i < arrayList.size(); i++) {
            String chipText = arrayList.get(i);

            // Inflate chip from layout
            Chip chip = (Chip) LayoutInflater.from(ReservationActivity.this).inflate(
                    R.layout.chip_layout, chipGroup, false);
            chip.setText(chipText);
            chip.setCheckable(true);
            chipGroup.addView(chip);  // Add to the chip group

            // Mark the first chip id
            if (i == 0 && chipGroup != binding.chipGroupOccasion && !isEditMode)
                firstChip = chip;
        }

        // Checked the first chip on each chip group by default
        if (firstChip != null) {
            chipGroup.check(firstChip.getId());
        }
    }

    // Edit mode: set selected chip
    private void setUpSelectedChip(ChipGroup chipGroup, String targetText) {
        if (targetText == null)
            return;

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (targetText.equals(chip.getText().toString())) {
                // Check the chip
                chipGroup.check(chip.getId());
                return;
            }
        }
    }

    // Edit mode: set selected DatePicker
//    private void setSelectedDatePicker(String formatted) {
//        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM d EEE", Locale.ENGLISH);
//
//        // Parse the formatted date -> LocalDate
//        LocalDate parsed = LocalDate.parse(formatted, parser);
//
//        LocalDate selectedDate = LocalDate.of(
//                LocalDate.now().getYear(), parsed.getMonth(), parsed.getDayOfMonth());
//
//        Log.d("Selected Date Picker", selectedDate.toString());
//
//        // Update DatePicker (month is 0-based)
//        binding.datePicker.updateDate(
//                selectedDate.getYear(),
//                selectedDate.getMonthValue(),
//                selectedDate.getDayOfMonth()
//        );
//    }
}