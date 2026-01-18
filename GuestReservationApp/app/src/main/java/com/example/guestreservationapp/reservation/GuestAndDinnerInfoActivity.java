package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityGuestAndDinnerInfoBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.guestreservationapp.Guest;
import com.example.restaurant_reservation_lib.entity.Reservation;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuestAndDinnerInfoActivity extends BaseValidatedActivity {
    private ActivityGuestAndDinnerInfoBinding binding;
    private String firstName, lastName, phoneNumber, occasion;
    private String selectedCountryCode;
    private boolean isValidPhone = false,
            firstNameNotEmpty = false,
            lastNameNotEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestAndDinnerInfoBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());

        binding.editFirstName.addTextChangedListener(inputFieldWatcher);
        binding.editLastName.addTextChangedListener(inputFieldWatcher);
        binding.editPhone.addTextChangedListener(inputFieldWatcher);

        // Create singleton instance
        Reservation reservation = Reservation.getInstance();
        Guest guest = Guest.getInstance();

        // Assign values from the Guest obj
        firstName = guest.getFirstName();
        lastName = guest.getLastName();
        occasion = reservation.getOccasion();
//        String phoneWithCountryCode = guest.getPhoneNumber();
//
//        // Split into country code and phone number
//        Pair<String, String> splitResult = splitPhone(phoneWithCountryCode);
//        selectedCountryCode = splitResult.first;
//        phoneNumber = splitResult.second;

        // Setup Spinner to matching country code
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.country_codes,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCountryCode.setAdapter(adapter);
        // Select the position of the spinner as initialization
//        int positon = adapter.getPosition(selectedCountryCode);
//        binding.spinnerCountryCode.setSelection(Math.max(positon, 0));  // 0: fallback if not found

        // Set value for these textView
        binding.editFirstName.setText(firstName);
        binding.editLastName.setText(lastName);
        binding.editEmail.setText(guest.getEmail());
        phoneNumber = guest.getPhoneNumber();
//        binding.editPhone.setText(phoneNumber);

        // Initialize array list
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
        // Set selected chip
        if (occasion != null) {
            for (int i = 0; i < binding.chipGroupOccasion.getChildCount(); i++) {
                Chip chip = (Chip) binding.chipGroupOccasion.getChildAt(i);
                if (occasion.equals(chip.getText().toString())) {
                    binding.chipGroupOccasion.check(chip.getId());
                    return;
                }
            }
        }

        // Close Editing Form and back to main page
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Select country code
        binding.spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedCountryCode = adapterView.getItemAtPosition(pos).toString();
                if (phoneNumber != null)
                    // re-validate when code changes
                    isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);

                binding.btnUpdate.setEnabled(firstNameNotEmpty && lastNameNotEmpty && isValidPhone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCountryCode = "+44";
            }
        });

        // Get a selected chip from Time field
        binding.chipGroupOccasion.setOnCheckedStateChangeListener(
                (chipGroup, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                // Uncheck the chip
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

            guest.setFirstName(firstName);
            guest.setLastName(lastName);
            guest.setPhoneNumber(String.format("(%s) %s", selectedCountryCode, phoneNumber));
            reservation.setOccasion(occasion);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    // Helper: Split country code and phone number
    private Pair<String, String> splitPhone(String fullPhone) {
        Pattern pattern = Pattern.compile("\\(\\+(\\d+)\\)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(fullPhone);

        // Match the pattern
        if (matcher.matches()) {
            String code = "+" + matcher.group(1);
            String number = matcher.group(2);
            return new Pair<>(code, number);
        }

        // Error case
        return new Pair<>("+44", "");
    }

    // Text input monitor
    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            firstName = binding.editFirstName.getText().toString().trim();
            lastName = binding.editLastName.getText().toString().trim();
            phoneNumber = binding.editPhone.getText().toString().trim();

            firstNameNotEmpty = isNotFieldEmpty(firstName, binding.textInputFirstName, "Please enter your first name");
            lastNameNotEmpty = isNotFieldEmpty(lastName, binding.textInputLastName, "Please enter your last name");
            isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);

            binding.btnUpdate.setEnabled(firstNameNotEmpty && lastNameNotEmpty && isValidPhone);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };
}