package com.example.adminreservationmanagementapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.adminreservationmanagementapp.databinding.ActivityAddMenuItemBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.ChipGroup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddMenuItemActivity extends AppCompatActivity {
    private ActivityAddMenuItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMenuItemBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        binding.imgBtnClose.setOnClickListener(viewClose -> finish());

        // Choose image button click
        binding.btnChooseImg.setOnClickListener(viewUploadImg ->
                ImagePicker.with(AddMenuItemActivity.this)
//                        .crop()
                .compress(1024)
                .maxResultSize(200, 200)
                .start());

        // Meal Time selection changes
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
}