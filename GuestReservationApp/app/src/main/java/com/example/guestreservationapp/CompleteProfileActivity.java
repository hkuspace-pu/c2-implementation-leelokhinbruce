package com.example.guestreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.restaurant_reservation_lib.accessing_data.AuthApi;
import com.example.guestreservationapp.databinding.ActivityCompleteProfileBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.restaurant_reservation_lib.request.LoginRequest;
import com.example.restaurant_reservation_lib.request.RegisterRequest;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileActivity extends BaseValidatedActivity {
    private ActivityCompleteProfileBinding binding;
    private String firstName, lastName, phoneNumber;
    private String selectedCountryCode = "+44";  // default
    private boolean isValidPhone = false, firstNameNotEmpty = false, lastNameNotEmpty = false;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Get data from CreateAccountActivity
        String username = getIntent().getStringExtra(CreateAccountActivity.EXTRA_USERNAME);
        String email = getIntent().getStringExtra(CreateAccountActivity.EXTRA_EMAIL);
        String password = getIntent().getStringExtra(CreateAccountActivity.EXTRA_PASSWORD);

        binding.editFirstName.addTextChangedListener(inputFieldWatcher);
        binding.editLastName.addTextChangedListener(inputFieldWatcher);
        binding.editPhone.addTextChangedListener(inputFieldWatcher);

        // Setup Spinner
        // Country Code Adapter
        ArrayAdapter<CharSequence> countryCodeAdapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.country_codes,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        countryCodeAdapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCountryCode.setAdapter(countryCodeAdapter);
        // Gender Adapter
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.genders,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        genderAdapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerGender.setAdapter(genderAdapter);

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Select country code
        binding.spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedCountryCode = adapterView.getItemAtPosition(pos).toString();
                if (phoneNumber != null)
                    // re-validate when code changes
                    isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);
                binding.btnComplete.setEnabled(firstNameNotEmpty && lastNameNotEmpty && isValidPhone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCountryCode = "+44";
            }
        });

        // Complete button click
        binding.btnComplete.setEnabled(false);
        binding.btnComplete.setOnClickListener(viewComplete -> {
            String gender = binding.spinnerGender.getSelectedItem().toString();

            // Show alert dialog before create account
            new MaterialAlertDialogBuilder(CompleteProfileActivity.this)
                    .setTitle("Register Account")
                    .setMessage("Are you sure the correct information of your account?")
                    .setCancelable(false)
                    .setPositiveButton("Sign In", (dialog, which) -> {
                        isLoading(true);  // Loading progress bar
                        String fullPhone = String.format("(%s) %s", selectedCountryCode, phoneNumber);
                        RegisterRequest registerRequest =
                                new RegisterRequest(username, email, password, firstName, lastName, fullPhone, gender);
                        // Complete register and save user account by making API call
                        executorService.execute(() ->
                                createUserAccount(registerRequest, email, password));
                    })
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.cancel()).show();
        });
    }

    // Insert user data into the local database
    private void createUserAccount(RegisterRequest registerRequest, String email, String password) {
        AuthApi api = ApiClient.getClient(null).create(AuthApi.class);
        Call<Map<String, String>> registerCall = api.registerUser(registerRequest);

        // Sign Up
        registerCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    mainHandler.post(() ->
                            Toast.makeText(CompleteProfileActivity.this, "Registration successful", Toast.LENGTH_SHORT).show());

                    // Auto Sign In after registration
                    LoginRequest loginRequest = new LoginRequest(email, password);
                    Call<Map<String, String>> loginCall = api.loginUser(loginRequest);  // Returns token

                    loginCall.enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.isSuccessful()) {
                                // Encrypt and save tokens
                                String accessToken = response.body().get("access_token");

                                // Store tokens in DataStore
                                saveToken(accessToken, () -> {
                                    // Starting main screen
                                    Toast.makeText(CompleteProfileActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(new Intent(CompleteProfileActivity.this, MainActivity.class));
                                    startActivity(intent);
                                    finish();
                                });
                            } else {
                                mainHandler.post(() ->
                                        Toast.makeText(CompleteProfileActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            Log.e("createUserAccount", "Login network error: " + t.getMessage());
                        }
                    });
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(CompleteProfileActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("createUserAccount", "Registration network error: " + t.getMessage());
            }
        });

        mainHandler.post(() -> isLoading(false));
    }

    // Monitor input fields
    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            firstName = binding.textInputFirstName.getEditText().getText().toString().trim();
            lastName = binding.textInputLastName.getEditText().getText().toString().trim();
            phoneNumber = binding.editPhone.getText().toString().trim();

            firstNameNotEmpty = isNotFieldEmpty(firstName, binding.textInputFirstName, "Please enter your first name");
            lastNameNotEmpty = isNotFieldEmpty(lastName, binding.textInputLastName, "Please enter your last name");
            isValidPhone = validPhoneNumber(phoneNumber, selectedCountryCode, binding.textInputPhone);

            binding.btnComplete.setEnabled(firstNameNotEmpty && lastNameNotEmpty && isValidPhone);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };

    // Disable anything during loading data
    private void isLoading(boolean status) {
        if (status) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnComplete.setClickable(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnComplete.setClickable(true);
        }
    }
}