package com.example.restaurant_reservation_lib;

import android.text.TextUtils;
import android.util.Patterns;

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

    // Validate email: matches pattern
    protected boolean validEmail(String email, TextInputLayout emailTextField) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextField.setError(null);
            return true;
        } else {
            emailTextField.setError("Invalid email pattern");
            return false;
        }
    }

    // Validate password: strong
    protected boolean validNewPassword(String password, TextInputLayout passwordTextField) {
        if (password.length() >= 8) {
            boolean hasUppercase = !password.equals(password.toUpperCase());
            boolean hasLowercase = !password.equals(password.toLowerCase());
            boolean hasDigital = password.matches(".*\\d.*");
            boolean hasSymbol = password.matches(".*[^a-zA-Z0-9].*");

            if (hasUppercase && hasLowercase && hasDigital && hasSymbol) {
                passwordTextField.setHelperText("Strong Password");
                passwordTextField.setError(null);
                return true;
            } else {
                passwordTextField.setHelperText(null);
                passwordTextField.setError("Password must include at least 1 uppercase letter, 1 lowercase letter, 1 digital number, and 1 special symbol");
                return false;
            }
        } else {
            passwordTextField.setHelperText("Enter minimum 8 char");
            passwordTextField.setError(null);
            return false;
        }
    }
}
