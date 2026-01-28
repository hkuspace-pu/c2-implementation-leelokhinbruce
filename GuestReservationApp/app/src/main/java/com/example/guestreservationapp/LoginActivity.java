package com.example.guestreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.guestreservationapp.accessing_data.GuestInfoApi;
import com.example.restaurant_reservation_lib.accessing_data.AuthApi;
import com.example.guestreservationapp.databinding.ActivityLoginBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.restaurant_reservation_lib.request.LoginRequest;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends BaseValidatedActivity {
    private ActivityLoginBinding binding;
    private String emailOrUsername, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Auto-login if session exists (token stores in DataStore already)
        String token = getAccessToken();
        if (token != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Monitor input fields
        binding.editEmailOrUsername.addTextChangedListener(inputFieldWatcher);
        binding.editPasswd.addTextChangedListener(inputFieldWatcher);

        binding.btnLogin.setEnabled(false);
        // Login button click
        binding.btnLogin.setOnClickListener(viewLogin ->
                loginUser(emailOrUsername, password));

        // Go to Register screen
        binding.linkRegister.setOnClickListener(viewRegister -> {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void loginUser(String usernameOrEmail, String password) {
        LoginRequest loginRequest = new LoginRequest(usernameOrEmail, password);
        AuthApi api = ApiClient.getClient(null).create(AuthApi.class);
        Call<Map<String, String>> call = api.loginUser(loginRequest);  // Returns token
        // Handle the response in Callback
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                // backend response: (return new ResponseEntity<>(...))
                if (response.isSuccessful()) {
                    Map<String, String> res = response.body();
                    String token = res.get("access_token");
                    String role = res.get("role");

                    if (!"ROLE_GUEST".equals(role)) {
                        Toast.makeText(LoginActivity.this, "This app is for guests only. Please use the staff app to login.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Store tokens in DataStore
                    saveToken(token, () -> {
                        // Go to main screen
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(new Intent(LoginActivity.this, MainActivity.class));
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("loginUser", "Network error: " + t.getMessage());
            }
        });
    }

    // Monitor input fields
    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            emailOrUsername = binding.textInputEmailOrUsername.getEditText().getText().toString().trim();
            password = binding.textInputPasswd.getEditText().getText().toString().trim();

            boolean emailNotEmpty = isNotFieldEmpty(emailOrUsername, binding.textInputEmailOrUsername, "Please enter email or username");
            boolean passwordNotEmpty = isNotFieldEmpty(password, binding.textInputPasswd, "Please enter password");

            binding.btnLogin.setEnabled(emailNotEmpty && passwordNotEmpty);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };
}