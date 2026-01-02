package com.example.adminreservationmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminreservationmanagementapp.databinding.ActivitySpecificMenuBinding;
import com.example.adminreservationmanagementapp.viewmodel.MenuItemViewModel;
import com.example.restaurant_reservation_lib.adapter.MenuItemAdapter;
import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpecificMenuActivity extends AppCompatActivity {
    private ActivitySpecificMenuBinding binding;
    private MenuItemViewModel menuItemViewModel;
    private ExecutorService executorService;
    private static final int ADD_ITEM_REQUEST = 1;
    private static final int EDIT_ITEM_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecificMenuBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();

        // Get the string from MenuFragment
        binding.textMenuTitle.setText(getIntent().getStringExtra("screen_title"));
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Click Add Item image button
        binding.imgBtnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpecificMenuActivity.this, AddMenuItemActivity.class);
                startActivityForResult(intent, ADD_ITEM_REQUEST);
            }
        });

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

        if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK && data != null) {
            String foodName = data.getStringExtra(AddMenuItemActivity.EXTRA_FOOD_NAME);
            double price = data.getDoubleExtra(AddMenuItemActivity.EXTRA_PRICE, 0);
            String category = data.getStringExtra(AddMenuItemActivity.EXTRA_CATEGORY);
            String mealTime = data.getStringExtra(AddMenuItemActivity.EXTRA_MEAL_TIME);
            boolean isPromotion = data.getBooleanExtra(AddMenuItemActivity.EXTRA_IS_PROMOTION, false);
            Date createdDate = new Date();
            createdDate.setTime(data.getLongExtra(AddMenuItemActivity.EXTRA_CREATED_DATE, -1));
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
            Log.d("SpecialMenuActivity", "Get menu item details: \nFood Name: " + menuItem.getFoodName() + "\nPrice: " + menuItem.getPrice() + "\nMeal Time: " + menuItem.getMealTime());
            Toast.makeText(this, "Menu item saved", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("SpecialMenuActivity", "Get menu item failed");
            Toast.makeText(this, "Menu item not saved", Toast.LENGTH_SHORT).show();
        }
    }
}