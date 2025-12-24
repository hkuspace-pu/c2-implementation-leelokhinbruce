package com.example.adminreservationmanagementapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.adminreservationmanagementapp.databinding.ActivityAddMenuItemBinding;
import com.example.restaurant_reservation_lib.BaseValidatedActivity;
import com.example.restaurant_reservation_lib.MenuItemViewModel;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.ChipGroup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddMenuItemActivity extends BaseValidatedActivity {
    private ActivityAddMenuItemBinding binding;
    private MenuItemViewModel menuItemViewModel;
    private String foodNameStr, priceStr;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMenuItemBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Initialize ViewModel
        menuItemViewModel = new ViewModelProvider(this).get(MenuItemViewModel.class);

        binding.imgBtnClose.setOnClickListener(viewClose -> finish());

        // Choose image button click
        binding.btnChooseImg.setOnClickListener(viewUploadImg ->
                ImagePicker.with(AddMenuItemActivity.this)
//                        .crop()
                .compress(1024)
                .maxResultSize(200, 200)
                .start());

        // Submit button is disable if food name and price is empty
        binding.btnSubmit.setEnabled(false);
        binding.editFoodName.addTextChangedListener(inputFieldWatcher);
        binding.editPrice.addTextChangedListener(inputFieldWatcher);

        // Submit button click
        binding.btnSubmit.setOnClickListener(viewSubmit -> submitMenuItem());
    }

    // Upload photo via URL
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            binding.imgPhoto.setImageURI(imageUri);
        } else {
            binding.imgPhoto.setImageDrawable(getResources().getDrawable(com.example.restaurant_reservation_lib.R.drawable.photo_icon));
        }
    }

    // Submit menu item
    private void submitMenuItem() {
        double price = Double.parseDouble(priceStr);
        String category = binding.spinnerCategory.getSelectedItem().toString();
        boolean isPromotion = binding.switchIsPromotion.isChecked();

        // Build MenuItem obj
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
            foodNameStr = binding.textInputFoodName.getEditText().getText().toString().trim();
            priceStr = binding.textInputPrice.getEditText().getText().toString().trim();

            boolean isFoodNameValid = isNotFieldEmpty(foodNameStr, binding.textInputFoodName, "Please enter food name");
            boolean isPriceValid = isNotFieldEmpty(priceStr, binding.textInputPrice, "Please enter price");

            binding.btnSubmit.setEnabled(isFoodNameValid && isPriceValid);
        }
    };
}