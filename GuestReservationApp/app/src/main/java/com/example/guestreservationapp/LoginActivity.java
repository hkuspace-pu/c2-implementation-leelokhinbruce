package com.example.guestreservationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guestreservationapp.accessing_data.AuthApi;
import com.example.guestreservationapp.databinding.ActivityLoginBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.guestreservationapp.request.LoginRequest;
import com.example.guestreservationapp.request.RegisterRequest;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.restaurant_reservation_lib.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseValidatedActivity {
    private ActivityLoginBinding binding;
    private String emailOrUsername, password;
    private ExecutorService executorService;
    private Handler mainHandler;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Init shared preferences and support method for editing
        session = new SessionManager(this);
        // Check if session is exists then one current user is logged
        if (session.isLoggedIn()) {
            // Has session
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        Log.e("LoginActivity", "Session: " + session.getToken());

        // Monitoring input fields
        binding.editEmailOrUsername.addTextChangedListener(inputFieldWatcher);
        binding.editPasswd.addTextChangedListener(inputFieldWatcher);

        binding.btnLogin.setEnabled(false);
        // Login button click
        binding.btnLogin.setOnClickListener(viewLogin -> {
            executorService.execute(() -> loginUser(emailOrUsername, password));
        });

        // Go to Register screen
        binding.linkRegister.setOnClickListener(viewRegister -> {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    // Sign In for validating user credential
    private void loginUser(String usernameOrEmail, String password) {
        LoginRequest loginRequest = new LoginRequest(usernameOrEmail, password);
        AuthApi api = ApiClient.getClient().create(AuthApi.class);
        Call<String> call = api.loginUser(loginRequest);  // Returns token

        // Send POST request
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    // Save session in shared preferences
                    session.saveSession(token, usernameOrEmail);
                    Log.d("loginUser", "Token: " + token);

                    // Starting main screen
                    mainHandler.post(() -> {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    });
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("login", "Network error: " + t.getMessage());
            }
        });
    }

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