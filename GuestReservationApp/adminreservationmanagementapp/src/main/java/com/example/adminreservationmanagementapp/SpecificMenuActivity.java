package com.example.adminreservationmanagementapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adminreservationmanagementapp.databinding.ActivitySpecificMenuBinding;
import com.example.adminreservationmanagementapp.viewmodel.MenuItemViewModel;
import com.example.restaurant_reservation_lib.adapter.MenuItemAdapter;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpecificMenuActivity extends AppCompatActivity {
    private ActivitySpecificMenuBinding binding;
    private MenuItemViewModel menuItemViewModel;
    private static final int ADD_ITEM_REQUEST = 1, EDIT_ITEM_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecificMenuBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Get the string from MenuFragment
        String mealTimeFilter = getIntent().getStringExtra("screen_title");
        binding.textMenuTitle.setText(mealTimeFilter);

        // Close the Activity
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Click Add Item image button
        binding.imgBtnAddItem.setOnClickListener(viewAddItem -> {
            Intent intent = new Intent(SpecificMenuActivity.this, AddMenuItemActivity.class);
            startActivityForResult(intent, ADD_ITEM_REQUEST);
        });

        // Set RecycleView
        binding.recycleMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleMenu.setHasFixedSize(true);

        // Initialize an adapter for the recycle view
        final MenuItemAdapter adapter = new MenuItemAdapter(true);  // true - option button is visible

        // Set item action listener for item of recycle view
        // Adapter as a listener to listen which menu item is being activated (clicked)
        // Display dialog when clicked a hidden button on an item
        adapter.setOnItemActionListener(new MenuItemAdapter.OnItemActionListener() {
            @Override
            public void onEdit(MenuItem menuItem) {
                Intent intent = new Intent(SpecificMenuActivity.this, AddMenuItemActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra(AddMenuItemActivity.EXTRA_ID, menuItem.getId());
                intent.putExtra(AddMenuItemActivity.EXTRA_FOOD_NAME, menuItem.getFoodName());
                intent.putExtra(AddMenuItemActivity.EXTRA_PRICE, menuItem.getPrice());
                intent.putExtra(AddMenuItemActivity.EXTRA_CATEGORY, menuItem.getCategory());
                intent.putExtra(AddMenuItemActivity.EXTRA_MEAL_TIME, menuItem.getMealTime());
                intent.putExtra(AddMenuItemActivity.EXTRA_IS_PROMOTION, menuItem.isPromotion());

                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }

            @Override
            public void onDelete(MenuItem menuItem) {
                new MaterialAlertDialogBuilder(SpecificMenuActivity.this)
                        .setTitle("Delete " + menuItem.getFoodName())
                        .setMessage("Are you sure to delete the menu item?")
                        .setPositiveButton("Delete", (dialog, which) ->
                                menuItemViewModel.deleteMenuItem(menuItem))
                        .setNegativeButton("Cancel", (dialog, which) ->
                                dialog.cancel()).show();
            }
        });

        // Close open actions when tapping outside any item
        binding.getRoot().setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                adapter.closeCurrentOpenItem();
            return false;  // Allow touch to pass through
        });

        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleMenu.setAdapter(adapter);

        // Initialize ViewModel
        menuItemViewModel = new ViewModelProvider(this).get(MenuItemViewModel.class);
        // Observe the change from the menu item list
        // Get all the menu items from view model
//        menuItemViewModel.getAllMenuItems().observe(this, menuItems
//                -> adapter.setMenuItems(menuItems));
        menuItemViewModel.getAllMenuItems().observe(this, allMenuItems -> {
            // Filter by mealTime
            List<MenuItem> filtered = new ArrayList<>();
            for (MenuItem item : allMenuItems) {
                if (mealTimeFilter.equals(item.getMealTime())) {
                    filtered.add(item);
                }
            }
            // List filtered items
            adapter.setMenuItems(filtered);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK = -1

        // Get data from AddMenuItemActivity after added
        if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK && data != null) {
            String foodName = data.getStringExtra(AddMenuItemActivity.EXTRA_FOOD_NAME);
            double price = data.getDoubleExtra(AddMenuItemActivity.EXTRA_PRICE, 0);
            String category = data.getStringExtra(AddMenuItemActivity.EXTRA_CATEGORY);
            String mealTime = data.getStringExtra(AddMenuItemActivity.EXTRA_MEAL_TIME);
            boolean isPromotion = data.getBooleanExtra(AddMenuItemActivity.EXTRA_IS_PROMOTION, false);
            Date createdDate = new Date();
            createdDate.setTime(data.getLongExtra(AddMenuItemActivity.EXTRA_CREATED_DATE, -1));
//            Bitmap photoBitmap = (Bitmap) data.getParcelableExtra(EXTRA_PHOTO);

            String mealType = data.getStringExtra(AddMenuItemActivity.EXTRA_MEAL_TYPE);
            if (mealType != null) {
                foodName = foodName + " " + mealType + " Meal";
            }

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

            Log.d("SpecialMenuActivity onActivityResult()", "Result code: " + resultCode + "\nItem Id: " + menuItem.getId());
            Toast.makeText(this, "Menu item saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK && data != null) {
            long id = data.getLongExtra(AddMenuItemActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Menu item can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

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
            menuItem.setId(id);

            menuItemViewModel.updateMenuItem(menuItem);
            Log.d("SpecialMenuActivity onActivityResult()", "Result code: " + resultCode + "\nItem Id: " + menuItem.getId());
            Toast.makeText(this, "Menu item updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("SpecialMenuActivity", "Get menu item failed: " + resultCode);
            Toast.makeText(this, "Menu item not saved", Toast.LENGTH_SHORT).show();
        }
    }
}