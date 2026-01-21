package com.example.restaurant_reservation_lib;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String TOKEN_KEY = "jwt_token";
    private static final String USERNAME_OR_EMAIL_KEY = "username_or_email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Constructor
    public SessionManager(Context context) {
        // Initialize shared preferences
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Call method to edit values in shared preferences
        editor = sharedPreferences.edit();
    }

    // Save values in shared preferences
    public void saveSession(String token, String usernameOrEmail) {
        editor.putString(TOKEN_KEY, token);
        editor.putString(USERNAME_OR_EMAIL_KEY, usernameOrEmail);
        editor.apply();  // Save data with key and value
    }

    // Get data from shared preferences
    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public String getUsernameOrEmailKey() {
        return sharedPreferences.getString(USERNAME_OR_EMAIL_KEY, null);
    }

    // Auto-login if token is exist
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // Clear the data in shared preferences
    public void clearSession() {
        editor.clear();
        editor.apply();  // Apply empty data
    }
}
