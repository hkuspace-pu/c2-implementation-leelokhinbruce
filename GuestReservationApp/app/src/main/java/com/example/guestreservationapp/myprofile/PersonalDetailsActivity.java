package com.example.guestreservationapp.myprofile;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.ActivityPersonalDetailsBinding;

public class PersonalDetailsActivity extends AppCompatActivity {
    private ActivityPersonalDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        binding.imgBtnBack.setOnClickListener(viewBack -> finish());
    }
}