package com.example.adminreservationmanagementapp;

import static com.example.adminreservationmanagementapp.AddMenuItemActivity.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.internal.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminreservationmanagementapp.databinding.ActivitySpecificMenuBinding;
import com.example.restaurant_reservation_lib.MenuItemViewModel;
import com.example.restaurant_reservation_lib.adapter.MenuItemAdapter;
import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.Date;

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

        // Set RecycleView
        binding.recycleMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleMenu.setHasFixedSize(true);

        // Initialize an adapter for the recycle view
        final MenuItemAdapter adapter = new MenuItemAdapter(true);  // true - option button is visible
        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleMenu.setAdapter(adapter);

        // Initialize ViewModel
        menuItemViewModel = new ViewModelProvider(this).get(MenuItemViewModel.class);
        // Observe the change from the menu item list
        // Get all the menu items from view model
        menuItemViewModel.getAllMenuItems().observe(this, adapter::setMenuItems);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String foodName = data.getStringExtra(EXTRA_FOOD_NAME);
            double price = data.getDoubleExtra(EXTRA_PRICE, 0);
            String category = data.getStringExtra(EXTRA_CATEGORY);
            String mealTime = data.getStringExtra(EXTRA_MEAL_TIME);
            boolean isPromotion = data.getBooleanExtra(EXTRA_IS_PROMOTION, false);
            Date createdDate = new Date();
            createdDate.setTime(data.getLongExtra(EXTRA_CREATED_DATE, -1));
//            Bitmap photoBitmap = (Bitmap) data.getParcelableExtra(EXTRA_PHOTO);

            // Build a menu item
            MenuItem menuItem = new MenuItem.Builder(
                    foodName,
                    price,
                    category,
                    mealTime,
                    isPromotion,
                    createdDate,
                    true
            ).build();

            menuItemViewModel.insertMenuItem(menuItem);
            Toast.makeText(this, "Menu item saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Menu item not saved", Toast.LENGTH_SHORT).show();
        }
    }
}