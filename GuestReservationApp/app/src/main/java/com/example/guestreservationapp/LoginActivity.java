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

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.example.guestreservationapp.accessing_data.AuthApi;
import com.example.guestreservationapp.databinding.ActivityLoginBinding;
import com.example.guestreservationapp.mainpage.MainActivity;
import com.example.guestreservationapp.request.LoginRequest;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.restaurant_reservation_lib.DataStoreManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends BaseValidatedActivity {
    private ActivityLoginBinding binding;
    private String emailOrUsername, password;
    private ExecutorService executorService;
    private Handler mainHandler;
    // DataStore for secure preferences
    // Used to store key-value pairs in a file
    private RxDataStore<Preferences> dataStore;
    private String accessToken;

    // DataStore keys for tokens
    private static final Preferences.Key<String> KEY_ACCESS_TOKEN = PreferencesKeys.stringKey("access_token");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Init DataStore
        dataStore = DataStoreManager.getInstance(this);

        // Auto-login if session exists
        accessToken = getAccessToken(dataStore, KEY_ACCESS_TOKEN);
        if (accessToken != null) {
            Log.d("LoginActivity", "Get Token from DataStore: " + accessToken);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        binding.editEmailOrUsername.addTextChangedListener(inputFieldWatcher);
        binding.editPasswd.addTextChangedListener(inputFieldWatcher);

        binding.btnLogin.setEnabled(false);
        // Login button click
        binding.btnLogin.setOnClickListener(viewLogin ->
                executorService.execute(() -> loginUser(emailOrUsername, password)));

        // Go to Register screen
        binding.linkRegister.setOnClickListener(viewRegister -> {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void loginUser(String usernameOrEmail, String password) {
        LoginRequest loginRequest = new LoginRequest(usernameOrEmail, password);
        AuthApi api = ApiClient.getClient().create(AuthApi.class);
        Call<Map<String, String>> call = api.loginUser(loginRequest);  // Returns token

        // Handle the response in Callback
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                // backend response: (return new ResponseEntity<>(...))
                if (response.isSuccessful()) {
                    accessToken = response.body().get("access_token");

                    // Store tokens in DataStore
                    saveToken(accessToken, dataStore, KEY_ACCESS_TOKEN);

                    // Go to main screen
                    mainHandler.post(() -> {
                        Log.d("loginUser", "Token: " + accessToken);
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(new Intent(LoginActivity.this, MainActivity.class));
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    });
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("loginUser", "Network error: " + t.getMessage());
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