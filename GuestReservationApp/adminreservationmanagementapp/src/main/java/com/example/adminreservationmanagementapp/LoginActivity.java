package com.example.adminreservationmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminreservationmanagementapp.databinding.ActivityLoginBinding;
import com.example.adminreservationmanagementapp.mainpage.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.btnLogin.setOnClickListener(viewLogin ->
                startActivity(new Intent(LoginActivity.this, MainActivity.class)));
    }
}