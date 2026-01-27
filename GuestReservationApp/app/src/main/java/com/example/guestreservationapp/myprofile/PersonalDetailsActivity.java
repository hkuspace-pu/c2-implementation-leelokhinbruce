package com.example.guestreservationapp.myprofile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.accessing_data.GuestInfoApi;
import com.example.guestreservationapp.databinding.ActivityPersonalDetailsBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailsActivity extends BaseValidatedActivity {
    private ActivityPersonalDetailsBinding binding;
    private String firstName, lastName;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.genders,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerGender.setAdapter(adapter);

        // Get guest instance
        Guest guest = Guest.getInstance();
        // Set user data to layouts
        binding.editFirstName.setText(guest.getFirstName());
        binding.editLastName.setText(guest.getLastName());
        int spinnerPosition = adapter.getPosition(guest.getGender());
        binding.spinnerGender.setSelection(spinnerPosition);

        // Monitor input fields
        binding.editFirstName.addTextChangedListener(inputFieldWatcher);
        binding.editLastName.addTextChangedListener(inputFieldWatcher);

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Update button click
        binding.btnUpdate.setOnClickListener(viewUpdate -> new MaterialAlertDialogBuilder(PersonalDetailsActivity.this)
                .setTitle("Update Profile Details")
                .setMessage("Are you sure to update your profile details?")
                .setPositiveButton("Update", (dialog, which) -> {
                    String gender = binding.spinnerGender.getSelectedItem().toString();
                    executorService.execute(() -> saveProfileDetails(firstName, lastName, gender));
                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.cancel()).show());
    }

    // Update profile details
    private void saveProfileDetails(String firstName, String lastName, String gender) {
        String token = getAccessToken();
        GuestInfoApi api = ApiClient.getClient(token).create(GuestInfoApi.class);

        // Get guest instance and set its values
        Guest guest = Guest.getInstance();
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setGender(gender);

        // Call POST request
        Call<String> call = api.updateProfileDetails(guest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mainHandler.post(() -> {
                        Toast.makeText(PersonalDetailsActivity.this, "Profile details updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(PersonalDetailsActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("saveProfileDetails", "Network error: " + t.getMessage());
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
            firstName = binding.textInputFirstName.getEditText().getText().toString().trim();
            lastName = binding.textInputLastName.getEditText().getText().toString().trim();

            boolean firstNameNotEmpty = isNotFieldEmpty(firstName, binding.textInputFirstName, "Please enter your first name");
            boolean lastNameNotEmpty = isNotFieldEmpty(lastName, binding.textInputLastName, "Please enter your last name");

            binding.btnUpdate.setEnabled(firstNameNotEmpty && lastNameNotEmpty);
        }
    };
}