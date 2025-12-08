package com.example.guestreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.databinding.ActivityLoginBinding;
import com.example.guestreservationapp.mainpage.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.linkRegister.setOnClickListener(viewRegister -> {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(viewHome ->
                startActivity(new Intent(LoginActivity.this, MainActivity.class)));
    }
}