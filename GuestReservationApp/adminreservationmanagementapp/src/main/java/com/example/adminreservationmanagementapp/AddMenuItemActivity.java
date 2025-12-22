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
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.adminreservationmanagementapp.databinding.ActivityAddMenuItemBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

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

        binding.btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
//                ImagePicker.Companion.with(AddMenuItemActivity.this)
////                        .crop()
//                        .maxResultSize(200,200,true)
//                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
//                        .createIntentFromDialog(new Function1(){
//                            public Object invoke(Object var1){
//                                this.invoke((Intent)var1);
//                                return Unit.INSTANCE;
//                            }
//
//                            public void invoke(@NotNull Intent it){
//                                Intrinsics.checkNotNullParameter(it,"it");
//                                launcher.launch(it);
//                            }
//                        });
            }
        });
    }

//    ActivityResultLauncher<Intent> launcher
//            = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    Intent data = result.getData();
//                    if (data != null && data.getData() != null) {
//                        // Load image via URL
//                        Uri selectedImgUri = data.getData();
//                        Glide.with(AddMenuItemActivity.this)
//                                .load(selectedImgUri)
//                                .placeholder(com.example.restaurant_reservation_lib.R.drawable.photo_icon)
//                                .override(200, 200)
//                                .into(binding.imgPhoto);
////                        Bitmap selectedImgBitmap = null;
////                        try {
////                            // Use InputStream for safe loading to handle virtual URLs, without file path resolution
////                            InputStream inputStream = getContentResolver().openInputStream(selectedImgUri);
////                            selectedImgBitmap = BitmapFactory.decodeStream(inputStream);
////                            if (inputStream != null)
////                                inputStream.close();
////                        }
////                        catch (IOException e) {
////                            e.printStackTrace();
////                            Log.e("AddMenuItemActivity", "Error loading bitmap: " + e.getMessage());
////                        }
////                        binding.imgPhoto.setImageBitmap(selectedImgBitmap);
//                    }
//                }
//            }
//    );
}