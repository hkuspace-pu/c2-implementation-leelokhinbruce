package com.example.adminreservationmanagementapp.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.adminreservationmanagementapp.Staff;
import com.example.adminreservationmanagementapp.accessing_data.StaffInfoApi;
import com.example.adminreservationmanagementapp.databinding.ActivityAccountDetailsBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDetailsActivity extends BaseValidatedActivity {
    private ActivityAccountDetailsBinding binding;
    private String phoneNumber, countryCode;
    private boolean isValidPhone;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Get staff instance
        Staff staff = Staff.getInstance();

        // Split into country code and phone number
        Pair<String, String> splitResult = splitPhone(staff.getPhoneNumber());
        countryCode = splitResult.first;
        phoneNumber = splitResult.second;

        // Set staff data to layouts
        binding.editUsername.setText(staff.getUsername());
        binding.editEmail.setText(staff.getEmail());
        binding.editPhone.setText(phoneNumber);
        binding.editJobTitle.setText(staff.getPosition());
        binding.editWorkingBranch.setText(staff.getWorkingBranch());

        // Setup Spinner to matching country code
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.country_codes,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCountryCode.setAdapter(adapter);
        // Select the position of the spinner as initialization
        int positon = adapter.getPosition(countryCode);
        binding.spinnerCountryCode.setSelection(Math.max(positon, 0));  // 0: fallback if not found

        // Monitor input fields
        binding.editUsername.addTextChangedListener(inputFieldWatcher);
        binding.editPhone.addTextChangedListener(inputFieldWatcher);

        // Close
        binding.imgBtnClose.setOnClickListener(viewClose -> finish());

        // Select country code
        binding.spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                countryCode = adapterView.getItemAtPosition(pos).toString();
                if (phoneNumber != null) {
                    // re-validate when code changes
                    isValidPhone = validPhoneNumber(phoneNumber, countryCode, binding.textInputPhone);
                    binding.btnUpdate.setEnabled(isValidPhone);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                countryCode = "+44";
            }
        });

        // Update button click
        binding.btnUpdate.setOnClickListener(viewUpdate ->
                new MaterialAlertDialogBuilder(AccountDetailsActivity.this)
                .setTitle("Update Phone Number")
                .setMessage("Are your sure to update the phone number?")
                .setPositiveButton("Update", (dialog, which) -> {
                    String fullPhone = String.format("(%s) %s", countryCode, phoneNumber);
                    executorService.execute(() -> saveAccountDetails(fullPhone));
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()).show());
    }

    // Update staff account details
    private void saveAccountDetails(String phone) {
        String token = getAccessToken();
        // Create API endpoint
        StaffInfoApi api = ApiClient.getClient(token).create(StaffInfoApi.class);

        // Get staff instance and set its values
        Staff staff = Staff.getInstance();
        staff.setPhoneNumber(phone);

        // Call POST request
        Call<String> call = api.updateAccountDetails(staff);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mainHandler.post(() -> {
                        Toast.makeText(AccountDetailsActivity.this, "Account details updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(AccountDetailsActivity.this, "Updated failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("saveAccountDetails", "Network error: " + t.getMessage());
            }
        });
    }

    // Monitor input fields
    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            phoneNumber = binding.editPhone.getText().toString().trim();

            isValidPhone = validPhoneNumber(phoneNumber, countryCode, binding.textInputPhone);

            binding.btnUpdate.setEnabled(isValidPhone);
        }
    };
}