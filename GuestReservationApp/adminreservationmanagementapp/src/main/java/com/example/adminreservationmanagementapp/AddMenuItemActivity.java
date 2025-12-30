package com.example.adminreservationmanagementapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.adminreservationmanagementapp.databinding.ActivityAddMenuItemBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.restaurant_reservation_lib.MenuItemViewModel;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.entity.MenuMealTime;
import com.example.restaurant_reservation_lib.entity.MenuMealType;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddMenuItemActivity extends BaseValidatedActivity {
    private ActivityAddMenuItemBinding binding;
    private MenuItemViewModel menuItemViewModel;
    private String foodName, priceStr, mealTime;
    private Bitmap imageBitmap;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMenuItemBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Initialize ViewModel
        menuItemViewModel = new ViewModelProvider(this).get(MenuItemViewModel.class);
        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();

        binding.imgBtnClose.setOnClickListener(viewClose -> finish());

        // Choose image button click
        binding.btnChooseImg.setOnClickListener(viewUploadImg ->
                ImagePicker.with(AddMenuItemActivity.this)
//                        .crop()
                        .compress(1024)
                        .maxResultSize(200, 200)
                        .start());

        // Get a selected chip (MealTime) text
        binding.chipGroupMealTime.setOnCheckedChangeListener((chipGroup, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = findViewById(checkedId);
                mealTime = chip.getText().toString();
            }
        });

        // Submit button is disable if food name and price is empty
        binding.btnSubmit.setEnabled(false);
        binding.editFoodName.addTextChangedListener(inputFieldWatcher);
        binding.editPrice.addTextChangedListener(inputFieldWatcher);

        // Submit button click
        binding.btnSubmit.setOnClickListener(viewSubmit -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Add Menu Item")
                    .setMessage("Are you sure to add the menu item?")
                    // setCancelable(false): the Dialog Box will remain show even clicks on the outside
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        isLoading(true);  // Loading progress bar
                        executorService.execute(this::submitMenuItem);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                    }).show();  // Show the Alert Dialog Box
        });
    }

    // Upload photo via URL
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            binding.imgPhoto.setImageURI(imageUri);

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load photo", Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.imgPhoto.setImageDrawable(getResources().getDrawable(com.example.restaurant_reservation_lib.R.drawable.photo_icon));
        }
    }

    // Submit menu item
    private void submitMenuItem() {
        double price = Double.parseDouble(priceStr);
        String category = binding.spinnerCategory.getSelectedItem().toString();
        boolean isPromotion = binding.switchIsPromotion.isChecked();
        Date createDate = new Date();

        // Not allow no photo
        if (imageBitmap == null) {
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build MenuItem obj
        MenuItem menuItem = new MenuItem.Builder(
                foodName,
                price,
                category,
                mealTime,
                true,
                isPromotion,
                createDate
        ).setImage(imageBitmap).build();

        // Get selected Meal Types
        List<Integer> checkedMealTypeChipIds = binding.chipGroupMealType.getCheckedChipIds();
        List<Long> mealTypeIdList = new ArrayList<>();  // Store each selected chip id
        for (int chipId : checkedMealTypeChipIds) {
            long mealTypeId = getMealTypeIdFromChip(chipId);
            if (mealTypeId != -1)
                mealTypeIdList.add(mealTypeId);
        }

        // Interact with local database
        // Insert MenuItem
        menuItemViewModel.insertMenuItem(menuItem);  // Insert menu item data

        // Insert Meal Type junction
//        for (long mealTypeId : mealTypeIdList) {
//            MenuMealType menuMealType = new MenuMealType(menuItemId, mealTypeId);
//            menuItemViewModel.insertMenuMealType(menuMealType);
//        }

        // Success feedback on UI thread
        mainHandler.post(() -> {
            Toast.makeText(AddMenuItemActivity.this, "Menu item added successfully!", Toast.LENGTH_SHORT).show();
            isLoading(false);
            finish();
        });
    }

    // Helper: Map chip ID to MealTime database ID
    private long getMealTimeIdFromChip(int chipId) {
        if (chipId == binding.chipBreakfast.getId()) return 1L;
        if (chipId == binding.chipLunch.getId()) return 2L;
        if (chipId == binding.chipDinner.getId()) return 3L;
        return -1L;
    }

    // Helper: Map chip ID to MealType database ID
    private long getMealTypeIdFromChip(int chipId) {
        if (chipId == binding.chipNormalMeal.getId()) return 1L;
        if (chipId == binding.chipLarge.getId()) return 2L;
        if (chipId == binding.chipSpecialLarge.getId()) return 3L;
        return -1L;
    }

    // TextWatchers for each field to monitor textEditLayout
    TextWatcher inputFieldWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            foodName = binding.textInputFoodName.getEditText().getText().toString().trim();
            priceStr = binding.textInputPrice.getEditText().getText().toString().trim();

            boolean isFoodNameValid = isNotFieldEmpty(foodName, binding.textInputFoodName, "Please enter food name");
            boolean isPriceValid = isNotFieldEmpty(priceStr, binding.textInputPrice, "Please enter price");

            binding.btnSubmit.setEnabled(isFoodNameValid && isPriceValid);
        }
    };

    // Disable anything during loading data
    private void isLoading(boolean status) {
        if (status) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSubmit.setClickable(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnSubmit.setClickable(true);
        }
    }
}