package com.example.adminreservationmanagementapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adminreservationmanagementapp.databinding.ActivityAddMenuItemBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddMenuItemActivity extends BaseValidatedActivity {
    private ActivityAddMenuItemBinding binding;
    private String foodName, priceStr, mealType;
    private Bitmap imageBitmap;
    private boolean isEditMode = false;
    private ArrayAdapter<CharSequence> adapter;
    private Intent intent;
    private ExecutorService executorService;
    private Handler mainHandler;

    private final String ADD_SCREEN_TITLE = "Add Menu Item",
            EDIT_SCREEN_TITLE = "Edit Menu Item",
            SUBMIT_TEXT_BUTTON = "Submit",
            UPDATE_TEXT_BUTTON = "Update";

    public static final String EXTRA_ID = "com.example.adminreservationmanagementapp.EXTRA_ID";
    public static final String EXTRA_FOOD_NAME = "com.example.adminreservationmanagementapp.EXTRA_FOOD_NAME";
    public static final String EXTRA_PRICE = "com.example.adminreservationmanagementapp.EXTRA_PRICE";
    public static final String EXTRA_CATEGORY = "com.example.adminreservationmanagementapp.EXTRA_CATEGORY";
    public static final String EXTRA_IS_PROMOTION = "com.example.adminreservationmanagementapp.EXTRA_IS_PROMOTION";
    public static final String EXTRA_CREATED_DATE = "com.example.adminreservationmanagementapp.EXTRA_CREATED_DATE";
    public static final String EXTRA_PHOTO = "com.example.adminreservationmanagementapp.EXTRA_PHOTO";
    public static final String EXTRA_MEAL_TYPE = "com.example.adminreservationmanagementapp.EXTRA_MEAL_TYPES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMenuItemBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Setup Spinner
        adapter = ArrayAdapter.createFromResource(this,
                com.example.restaurant_reservation_lib.R.array.category,
                com.example.restaurant_reservation_lib.R.layout.spinner_selected_item);
        // Dropdown item layout
        adapter.setDropDownViewResource(com.example.restaurant_reservation_lib.R.layout.spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);

        // Get data from SpecificMenuActivity
        intent = getIntent();
        isEditMode = intent.getBooleanExtra("EDIT_MODE", false);
        if (intent.hasExtra(EXTRA_ID) && isEditMode) {
            setupEditMode();
        } else {
            setupAddMode();
        }

        // Close the Activity
        binding.imgBtnClose.setOnClickListener(viewClose -> finish());

        // Choose image button click
        binding.btnChooseImg.setOnClickListener(viewUploadImg ->
                ImagePicker.with(AddMenuItemActivity.this)
//                        .crop()
                        .compress(1024)
                        .maxResultSize(200, 200)
                        .start());

        // Get a selected chip (MealType) text
        binding.chipGroupMealType.setOnCheckedChangeListener((chipGroup, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = findViewById(checkedId);
                mealType = chip.getText().toString();
            }
        });

        binding.editFoodName.addTextChangedListener(inputFieldWatcher);
        binding.editPrice.addTextChangedListener(inputFieldWatcher);

        // Submit button click
        binding.btnSubmit.setOnClickListener(viewSubmit -> {
            String title = isEditMode ? EDIT_SCREEN_TITLE : ADD_SCREEN_TITLE;
            String message = isEditMode ? "Are you sure to update this item?" : "Are you sure to add the menu item?";
            String positiveBtn = isEditMode ? UPDATE_TEXT_BUTTON : SUBMIT_TEXT_BUTTON;

            // Show alert dialog before submit the item
            new MaterialAlertDialogBuilder(this)
                    .setTitle(title)
                    .setMessage(message)
                    // setCancelable(false): the Dialog Box will remain show even clicks on the outside
                    .setCancelable(false)
                    .setPositiveButton(positiveBtn, (dialog, which) -> {
                        double price = Double.parseDouble(priceStr);
                        String category = binding.spinnerCategory.getSelectedItem().toString();
                        boolean isPromotion = binding.switchIsPromotion.isChecked();
                        Date createDate = new Date();

                        isLoading(true);  // Loading progress bar
                        // save menu item data
                        executorService.execute(() ->
                                saveMenuItem(foodName, price, category, isPromotion, createDate, mealType));
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                    }).show();  // Show the Alert Dialog Box
        });
    }

    private void setupAddMode() {
        binding.textScreenTitle.setText(ADD_SCREEN_TITLE);
        binding.btnSubmit.setText(SUBMIT_TEXT_BUTTON);
        binding.chipGroupMealType.setVisibility(View.VISIBLE);
        // Submit button is disable if food name and price is empty
        binding.btnSubmit.setEnabled(false);
    }

    private void setupEditMode() {
        binding.textScreenTitle.setText(EDIT_SCREEN_TITLE);
        binding.btnSubmit.setText(UPDATE_TEXT_BUTTON);

        // Set for input fields
        binding.editFoodName.setText(intent.getStringExtra(EXTRA_FOOD_NAME));
        foodName = binding.editFoodName.getText().toString().trim();
        double price = intent.getDoubleExtra(EXTRA_PRICE, 0);
        binding.editPrice.setText(String.format("%.2f", price));
        priceStr = binding.editPrice.getText().toString().trim();

        String category = intent.getStringExtra(EXTRA_CATEGORY);
        // Set a category spinner
        int spinnerPosition = adapter.getPosition(category);
        binding.spinnerCategory.setSelection(spinnerPosition);

        // Set isPromotion switch toggle
        boolean isPromotion = intent.getBooleanExtra(EXTRA_IS_PROMOTION, false);
        binding.switchIsPromotion.setChecked(isPromotion);

        // Hidden the meal type section
        binding.chipGroupMealType.setVisibility(View.GONE);
        binding.btnSubmit.setEnabled(true);
    }

    // Photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null && resultCode == RESULT_OK) {
            // Upload photo via URL
            Uri imageUri = data.getData();
            binding.imgPhoto.setImageURI(imageUri);
//            try {
//                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Failed to load photo", Toast.LENGTH_SHORT).show();
//            }
        } else {
            binding.imgPhoto.setImageDrawable(getResources().getDrawable(com.example.restaurant_reservation_lib.R.drawable.photo_icon));
        }
    }

    // Save menu item data and pass them to SpecificMenuActivity
    private void saveMenuItem(String foodName, double price, String category, boolean isPromotion, Date nowDate, String mealType) {
        Intent data = new Intent();

        // Pass all menu item details via an intent
        data.putExtra(EXTRA_FOOD_NAME, foodName);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_CATEGORY, category);
        data.putExtra(EXTRA_IS_PROMOTION, isPromotion);
        data.putExtra(EXTRA_CREATED_DATE, nowDate.getTime());
//        data.putExtra(EXTRA_PHOTO, photoBitmap);
        data.putExtra(EXTRA_MEAL_TYPE, mealType);

        long id = intent.getLongExtra(EXTRA_ID, -1);
        if (id != -1 && isEditMode) {
            data.putExtra(EXTRA_ID, id);  // Passing id
        }

        // Setting result as data
        setResult(RESULT_OK, data);

        // Success feedback on UI thread
        mainHandler.post(() -> {
            String successMsg = isEditMode ? "Menu item updated finished" : "Menu item added finished";
            Toast.makeText(AddMenuItemActivity.this, successMsg, Toast.LENGTH_SHORT).show();
            isLoading(false);
            finish();
        });
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