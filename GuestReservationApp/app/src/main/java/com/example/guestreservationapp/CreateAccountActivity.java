package com.example.guestreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.databinding.ActivityCreateAccountBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;

public class CreateAccountActivity extends BaseValidatedActivity {
    private ActivityCreateAccountBinding binding;
    private String username, email, password, confirmedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.editUsername.addTextChangedListener(inputFieldWatcher);
        binding.editEmail.addTextChangedListener(inputFieldWatcher);
        binding.editPasswd.addTextChangedListener(inputFieldWatcher);
        binding.editConfirmPasswd.addTextChangedListener(inputFieldWatcher);

        // Register button click
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setOnClickListener(viewCompleteProfile -> {
            startActivity(new Intent(this, CompleteProfileActivity.class));
        });

        // Go to login screen
        binding.linkLogin.setOnClickListener(viewLogin -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            username = binding.textInputUsername.getEditText().getText().toString().trim();
            email = binding.textInputEmail.getEditText().getText().toString().trim();
            password = binding.textInputPasswd.getEditText().getText().toString().trim();
            confirmedPassword = binding.textInputConfirmPasswd.getEditText().getText().toString().trim();

            boolean validUsername = validUsername(username, binding.textInputUsername);
            boolean validEmail = validEmail(email, binding.textInputEmail);
            boolean validPassword = validNewPassword(password, binding.textInputPasswd);
            boolean matchPassword = matchPassword(password, confirmedPassword, binding.textInputConfirmPasswd);

            binding.btnRegister.setEnabled(validUsername && validEmail && validPassword && matchPassword);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };
}