package com.example.adminreservationmanagementapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.adminreservationmanagementapp.databinding.ActivityAddMenuItemBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddMenuItemActivity extends BaseValidatedActivity {
    private ActivityAddMenuItemBinding binding;
    private String foodName, priceStr, mealTime;
    private Bitmap imageBitmap;
    private ExecutorService executorService;
    private Handler mainHandler;

    public static final String EXTRA_ID = "com.example.adminreservationmanagementapp.EXTRA_ID";
    public static final String EXTRA_FOOD_NAME = "com.example.adminreservationmanagementapp.EXTRA_FOOD_NAME";
    public static final String EXTRA_PRICE = "com.example.adminreservationmanagementapp.EXTRA_PRICE";
    public static final String EXTRA_CATEGORY = "com.example.adminreservationmanagementapp.EXTRA_CATEGORY";
    public static final String EXTRA_MEAL_TIME = "com.example.adminreservationmanagementapp.EXTRA_MEAL_TIME";
    public static final String EXTRA_IS_PROMOTION = "com.example.adminreservationmanagementapp.EXTRA_IS_PROMOTION";
    public static final String EXTRA_CREATED_DATE = "com.example.adminreservationmanagementapp.EXTRA_CREATED_DATE";
    public static final String EXTRA_PHOTO = "com.example.adminreservationmanagementapp.EXTRA_PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMenuItemBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

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
//                        foodName = binding.editFoodName.getText().toString().trim();
//                        priceStr = binding.editPrice.getText().toString().trim();
                        double price = Double.parseDouble(priceStr);
                        String category = binding.spinnerCategory.getSelectedItem().toString();
                        boolean isPromotion = binding.switchIsPromotion.isChecked();
                        Date createDate = new Date();

                        // Not allow no photo
//                        if (imageBitmap == null) {
//                            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
//                            return;
//                        }

                        isLoading(true);  // Loading progress bar
                        Log.d("AddMenuItemActivity", "Ready to save menu item");
                        // save menu item data
                        executorService.execute(() -> saveMenuItem(foodName, price, category, mealTime, isPromotion, createDate));
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                    }).show();  // Show the Alert Dialog Box
        });
    }

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

    // Submit menu item
    private void saveMenuItem(String foodName, double price, String category, String mealTime, boolean isPromotion, Date nowDate) {
        Intent data = new Intent();

        // Pass all menu item details via an intent
        data.putExtra(EXTRA_FOOD_NAME, foodName);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_CATEGORY, category);
        data.putExtra(EXTRA_MEAL_TIME, mealTime);
        data.putExtra(EXTRA_IS_PROMOTION, isPromotion);
        data.putExtra(EXTRA_CREATED_DATE, nowDate.getTime());
//        data.putExtra(EXTRA_PHOTO, photoBitmap);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            // Passing id
            data.putExtra(EXTRA_ID, id);
        }

        // Setting result as data
        setResult(RESULT_OK, data);
        Log.d("AddMenuItemActivity", "Menu item details pass via Intent: \nFood Name: " + foodName + "\nPrice: $" + price + "\nMeal Time: " + mealTime);

        // Get selected Meal Types
//        List<Integer> checkedMealTypeChipIds = binding.chipGroupMealType.getCheckedChipIds();
//        List<Long> mealTypeIdList = new ArrayList<>();  // Store each selected chip id
//        for (int chipId : checkedMealTypeChipIds) {
//            long mealTypeId = getMealTypeIdFromChip(chipId);
//            if (mealTypeId != -1)
//                mealTypeIdList.add(mealTypeId);
//        }

        // Success feedback on UI thread
        mainHandler.post(() -> {
            Toast.makeText(AddMenuItemActivity.this, "Menu item added successfully!", Toast.LENGTH_SHORT).show();
            isLoading(false);
            Intent intent = new Intent(this, SpecificMenuActivity.class);
            intent.putExtra("screen_title", mealTime);
            startActivity(intent);
            finish();
            Log.d("AddMenuItemActivity", "Redirect to Specific Menu: " + mealTime);
        });
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