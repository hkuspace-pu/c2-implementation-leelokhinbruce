package com.example.guestreservationapp.myprofile;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.accessing_data.GuestInfoApi;
import com.example.guestreservationapp.databinding.ActivityPhoneNumberBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumberActivity extends BaseValidatedActivity {
    private ActivityPhoneNumberBinding binding;
    private String phoneNumber, countryCode;
    private boolean isValidPhone;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Get guest instance
        Guest guest = Guest.getInstance();

        // Split into country code and phone number
        Pair<String, String> splitResult = splitPhone(guest.getPhoneNumber());
        countryCode = splitResult.first;
        phoneNumber = splitResult.second;

        // Set phone number to the layout
        binding.editPhone.setText(phoneNumber);
        // Monitor input field
        binding.editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneNumber = charSequence.toString().trim();
                isValidPhone = validPhoneNumber(phoneNumber, countryCode, binding.textInputPhone);
                binding.btnUpdate.setEnabled(isValidPhone);
            }
        });

        // Setup Spinner to matching country code
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.country_codes,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCountryCode.setAdapter(adapter);
        // Select the position of the spinner as initialization
        int positon = adapter.getPosition(countryCode);
        binding.spinnerCountryCode.setSelection(Math.max(positon, 0));  // 0: fallback if not found

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

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
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(PhoneNumberActivity.this)
                        .setTitle("Update Phone Number")
                        .setMessage("Are your sure to update the phone number?")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fullPhone = String.format("(%s) %s", countryCode, phoneNumber);
                                executorService.execute(() -> savePhoneNumber(fullPhone));
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()).show();
            }
        });
    }

    // Update phone number
    private void savePhoneNumber(String fullPhone) {
        String token = getAccessToken();
        GuestInfoApi api = ApiClient.getClient(token).create(GuestInfoApi.class);

        Guest guest = Guest.getInstance();
        guest.setPhoneNumber(fullPhone);

        Call<String> call = api.updateProfileDetails(guest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mainHandler.post(() -> {
                        Toast.makeText(PhoneNumberActivity.this, "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(PhoneNumberActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("savePhoneNumber", "Network error: " + t.getMessage());
            }
        });
    }
}