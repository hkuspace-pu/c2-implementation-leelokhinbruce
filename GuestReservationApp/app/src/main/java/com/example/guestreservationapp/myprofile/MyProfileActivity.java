package com.example.guestreservationapp.myprofile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.databinding.ActivityMyProfileBinding;

public class MyProfileActivity extends AppCompatActivity {
    private ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.frameBtnPersonalDetails.setOnClickListener(viewPersonalDetails ->
                startActivity(new Intent(MyProfileActivity.this, PersonalDetailsActivity.class)));

        binding.frameBtnPhoneNumber.setOnClickListener(viewPhoneNum ->
                startActivity(new Intent(MyProfileActivity.this, PhoneNumberActivity.class)));

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}