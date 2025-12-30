package com.example.adminreservationmanagementapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminreservationmanagementapp.databinding.ActivitySpecificMenuBinding;
import com.example.restaurant_reservation_lib.MenuItemViewModel;
import com.example.restaurant_reservation_lib.adapter.MenuItemAdapter;

public class SpecificMenuActivity extends AppCompatActivity {
    private ActivitySpecificMenuBinding binding;
    private MenuItemViewModel menuItemViewModel;
//    private MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecificMenuBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Get the string from MenuFragment
        binding.textMenuTitle.setText(getIntent().getStringExtra("screen_title"));
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Initialize ViewModel
        menuItemViewModel = new ViewModelProvider(this).get(MenuItemViewModel.class);

        // Set RecycleView
        binding.recycleMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleMenu.setHasFixedSize(true);

        // Initialize an adapter for the recycle view
        final MenuItemAdapter adapter = new MenuItemAdapter(true);  // true - option button is visible
        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleMenu.setAdapter(adapter);

        // Observe the change from the menu item list
        menuItemViewModel.getAllMenuItems().observe(this, adapter::setMenuItems);
    }
}