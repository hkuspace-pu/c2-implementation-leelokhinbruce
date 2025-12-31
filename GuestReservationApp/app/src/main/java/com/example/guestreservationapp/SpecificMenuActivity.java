package com.example.guestreservationapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.guestreservationapp.databinding.ActivitySpecificMenuBinding;
import com.example.guestreservationapp.viewmodel.MenuItemViewModel;
import com.example.restaurant_reservation_lib.adapter.MenuItemAdapter;

public class SpecificMenuActivity extends AppCompatActivity {
    private ActivitySpecificMenuBinding binding;
    private MenuItemViewModel menuItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecificMenuBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Get the string from MenuFragment
        binding.textMenuTitle.setText(getIntent().getStringExtra("screen_title"));
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Set RecycleView
        binding.recycleMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleMenu.setHasFixedSize(true);

        // Assign the menu items to the adapter
        final MenuItemAdapter adapter = new MenuItemAdapter(false);  // false - option button is gone
        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleMenu.setAdapter(adapter);

        // Initialize ViewModel
        menuItemViewModel = new ViewModelProvider(this).get(MenuItemViewModel.class);
        // Observe the change from the menu item list
        menuItemViewModel.getAllMenuItems().observe(this, adapter::setMenuItems);
    }
}