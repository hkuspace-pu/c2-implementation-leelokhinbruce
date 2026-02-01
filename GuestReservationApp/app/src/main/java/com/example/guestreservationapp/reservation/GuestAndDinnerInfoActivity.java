package com.example.guestreservationapp.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.guestreservationapp.databinding.ActivityGuestAndDinnerInfoBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.guestreservationapp.Guest;

public class GuestAndDinnerInfoActivity extends BaseValidatedActivity {
    private ActivityGuestAndDinnerInfoBinding binding;
    private String firstName, lastName, phoneNumber;
    private String selectedCountryCode;
    private boolean isValidPhone, firstNameNotEmpty, lastNameNotEmpty;

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestAndDinnerInfoBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());

        binding.editFirstName.addTextChangedListener(inputFieldWatcher);
        binding.editLastName.addTextChangedListener(inputFieldWatcher);
        binding.editPhone.addTextChangedListener(inputFieldWatcher);

        // Create singleton instance
        Guest guest = Guest.getInstance();

        // Assign values from the Guest obj
        firstName = guest.getFirstName();
        lastName = guest.getLastName();
        String phoneWithCountryCode = guest.getPhoneNumber();

        // Split into country code and phone number
        Pair<String, String> splitResult = splitPhone(phoneWithCountryCode);
        selectedCountryCode = splitResult.first;
        phoneNumber = splitResult.second;

        // Set value for these textView
        binding.editFirstName.setText(guest.getFirstName());
        binding.editLastName.setText(guest.getLastName());
        binding.editEmail.setText(guest.getEmail());
        binding.editPhone.setText(splitResult.second);

        // Setup Spinner to matching country code
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.country_codes,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCountryCode.setAdapter(adapter);
        // Select the position of the spinner as initialization
        int positon = adapter.getPosition(selectedCountryCode);
        binding.spinnerCountryCode.setSelection(Math.max(positon, 0));  // 0: fallback if not found

        // Close Editing Form and back to main page
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Select country code
        binding.spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedCountryCode = adapterView.getItemAtPosition(pos).toString();
                if (phoneNumber != null) {
                    // re-validate when code changes
                    isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);
                    binding.btnUpdate.setEnabled(firstNameNotEmpty && lastNameNotEmpty && isValidPhone);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCountryCode = "+44";
            }
        });

        // Update button click
        binding.btnUpdate.setEnabled(true);
        binding.btnUpdate.setOnClickListener(viewUpdate -> {

            guest.setFirstName(firstName);
            guest.setLastName(lastName);
            guest.setPhoneNumber(String.format("(%s) %s", selectedCountryCode, phoneNumber));

            finish();
        });
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