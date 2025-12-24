package com.example.restaurant_reservation_lib;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class BaseValidatedActivity extends AppCompatActivity {
    // Check whether the field is empty
    protected boolean isNotFieldEmpty(String text, TextInputLayout textField, String errorMsg) {
        if (!TextUtils.isEmpty(text)) {
            textField.setError(null);
            return true;
        } else {
            textField.setError(errorMsg);
            return false;
        }
    }
}
