package com.example.guestreservationapp.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.accessing_data.GuestInfoApi;
import com.example.guestreservationapp.databinding.ActivityResetPasswordBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.restaurant_reservation_lib.request.ResetPasswordRequest;
import com.example.restaurant_reservation_lib.session_management.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseValidatedActivity {
    private ActivityResetPasswordBinding binding;
    private String currentPasswd, newPasswd;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Back
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Monitor input fields
        binding.editCurrentPasswd.addTextChangedListener(inputFieldWatch);
        binding.editNewPasswd.addTextChangedListener(inputFieldWatch);
        binding.editConfirmedNewPasswd.addTextChangedListener(inputFieldWatch);

        binding.btnReset.setEnabled(false);
        // Reset button click
        binding.btnReset.setOnClickListener(viewReset ->
                new MaterialAlertDialogBuilder(ResetPasswordActivity.this)
                .setTitle("Reset Password")
                .setMessage("Are you sure to reset your password?")
                .setPositiveButton("Reset", (dialog, i) -> {
                    isLoading(true);  // Loading progress bar
                    ResetPasswordRequest resetPasswordRequest =
                            new ResetPasswordRequest(currentPasswd, newPasswd);
                    executorService.execute(() -> resetPassword(resetPasswordRequest));
                })
                .setNegativeButton("Cancel", (dialog, i) ->
                        dialog.cancel()).show());
    }

    // Reset password
    private void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String token = new SessionManager(getApplicationContext()).getAccessToken();
        GuestInfoApi api = ApiClient.getClient(token).create(GuestInfoApi.class);

        Call<String> call = api.updatePassword(resetPasswordRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mainHandler.post(() -> {
                        // Return the success msg from backend
                        Toast.makeText(ResetPasswordActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        isLoading(false);
                        finish();
                    });
                } else {
                    mainHandler.post(() -> {
                        Toast.makeText(ResetPasswordActivity.this, "Current password incorrect", Toast.LENGTH_SHORT).show();
                        isLoading(false);
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mainHandler.post(() ->
                        Log.e("resetPassword", "Network error: " + t.getMessage()));
            }
        });
    }

    // Monitor input fields
    TextWatcher inputFieldWatch = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            currentPasswd = binding.textInputCurrentPasswd.getEditText().getText().toString().trim();
            newPasswd = binding.textInputNewPasswd.getEditText().getText().toString().trim();
            String confirmedPasswd = binding.textInputConfirmedNewPasswd.getEditText().getText().toString().trim();

            boolean currentPasswdNotEmpty =
                    isNotFieldEmpty(currentPasswd, binding.textInputCurrentPasswd, "Please enter your current password");
            boolean isValidNewPasswd = validNewPassword(newPasswd, binding.textInputNewPasswd);
            boolean matchPassword = matchPassword(newPasswd, confirmedPasswd, binding.textInputConfirmedNewPasswd);

            binding.btnReset.setEnabled(currentPasswdNotEmpty && isValidNewPasswd && matchPassword);
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
            binding.btnReset.setClickable(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnReset.setClickable(true);
        }
    }
}