package com.example.restaurant_reservation_lib;

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;

import com.example.restaurant_reservation_lib.session_management.SessionManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseValidatedActivity extends SessionManager {
    private static final Map<String, PhoneRule> PHONE_RULES = new HashMap<>();

    static {
        PHONE_RULES.put("+44", new PhoneRule(10, "^\\d{10}$"));  // UK: 10 digits
        PHONE_RULES.put("+1", new PhoneRule(10, "^\\d{10}$"));  // US/Canada: 10 digits
        PHONE_RULES.put("+852", new PhoneRule(8, "^\\d{8}$"));  // HK: 8 digits
        PHONE_RULES.put("+86", new PhoneRule(11, "^1[3-9]\\\\d{9}$\""));  // China: 11 digits, starts with 1
        PHONE_RULES.put("+81", new PhoneRule(10, "^\\d{10}$"));  // Japan: 10 digits
        PHONE_RULES.put("+33", new PhoneRule(9, "^\\d{9}$"));  // France: 9 digits
        PHONE_RULES.put("+91", new PhoneRule(10, "^[6-9]\\\\d{9}$"));  // India: 10 digits, starts with 6-9
    }

    private static class PhoneRule {
        final int phoneNumLength;
        final String regex;

        PhoneRule(int length, String regex) {
            phoneNumLength = length;
            this.regex = regex;
        }
    }

    // Validate phone: matches pattern based on the selected country code
    protected boolean validPhoneNumber(String phone, String countryCode, TextInputLayout phoneTextField) {
        PhoneRule rule = PHONE_RULES.get(countryCode);
        if (rule == null) {
            phoneTextField.setError("Unsupported country code");
            return false;
        }

        if (phone.length() == rule.phoneNumLength && phone.matches(rule.regex)) {
            phoneTextField.setError(null);
            return true;
        } else {
            String errorMsg = String.format("Phone number must be %d digits", rule.phoneNumLength);
            phoneTextField.setError(errorMsg);
            return false;
        }
    }

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

    // Validate username: not empty, 3+ chars, no spaces
    protected boolean validUsername(String username, TextInputLayout usernameTextField) {
        if (username.length() < 3) {
            usernameTextField.setError("Username's length must be longer than 3 chars");
            return false;
        } else if (username.contains(" ")) {
            usernameTextField.setError("Please remove spaces");
            return false;
        } else {
            usernameTextField.setError(null);
            return true;
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
                passwordTextField.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.deep_green)));
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

    // Match password
    protected boolean matchPassword(
            String password, String confirmedPassword, TextInputLayout confirmedPasswordTextField) {
        if (password.equals(confirmedPassword)) {
            confirmedPasswordTextField.setError(null);
            return true;
        } else {
            confirmedPasswordTextField.setError("Password is not match");
            return false;
        }
    }

    // Helper: Split country code and phone number
    protected Pair<String, String> splitPhone(String fullPhone) {
        Pattern pattern = Pattern.compile("\\(\\+(\\d+)\\)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(fullPhone);

        // Match the pattern
        if (matcher.matches()) {
            String code = "+" + matcher.group(1);
            String number = matcher.group(2);
            return new Pair<>(code, number);
        }

        // Error case
        return new Pair<>("+44", "");
    }
}
