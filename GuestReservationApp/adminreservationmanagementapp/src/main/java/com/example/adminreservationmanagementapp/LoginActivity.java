package com.example.adminreservationmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminreservationmanagementapp.databinding.ActivityLoginBinding;
import com.example.adminreservationmanagementapp.mainpage.MainActivity;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;

public class LoginActivity extends BaseValidatedActivity {
    private ActivityLoginBinding binding;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.editUsername.addTextChangedListener(inputFieldWatcher);
        binding.editPasswd.addTextChangedListener(inputFieldWatcher);

        binding.btnLogin.setEnabled(false);
        // Login button click
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            username = binding.textInputUsername.getEditText().getText().toString().trim();
            password = binding.textInputPasswd.getEditText().getText().toString().trim();

            boolean usernameNotEmpty = isNotFieldEmpty(username, binding.textInputUsername, "Please enter username");
            boolean passwordNotEmpty = isNotFieldEmpty(password, binding.textInputPasswd, "Please enter password");

            binding.btnLogin.setEnabled(usernameNotEmpty && passwordNotEmpty);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };
}