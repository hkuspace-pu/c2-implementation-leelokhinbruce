package com.example.restaurant_reservation_lib.session_management;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Single;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionManager {
    // DataStore for secure preferences
    // Used to store key-value pairs in a file
    private RxDataStore<Preferences> dataStore;

    // DataStore keys for tokens
    private static final Preferences.Key<String> KEY_ACCESS_TOKEN =
            PreferencesKeys.stringKey("access_token");

    public SessionManager(Context context) {
        dataStore = DataStoreManager.getInstance(context.getApplicationContext());
    }

    // Save encrypted token in DataStore
    public void saveToken(String accessToken) {
            // Encrypt tokens and user id
            String encryptedAccessToken = Encryptor.encrypt(accessToken);

            // Store in DataStore
            // updateDataAsync(): ensure thread-safe, atomic changes
            dataStore.updateDataAsync(prefs -> {
                MutablePreferences mutable = prefs.toMutablePreferences();
                mutable.set(KEY_ACCESS_TOKEN, encryptedAccessToken);
                // Store user id too
                return Single.just(mutable);
            }).subscribe();

        Log.d("saveTokens", "Token saved in DataStore");
    }

    // Retrieve and decrypt access token
    public String getAccessToken() {
        Preferences prefs = dataStore.data().blockingFirst();
        String encryptedToken = prefs.get(KEY_ACCESS_TOKEN);
        return encryptedToken != null ? Encryptor.decrypt(encryptedToken) : null;
    }

    // Remove token in DataStore
    public void clearToken() {
        dataStore.updateDataAsync(prefs -> {
            MutablePreferences mutable = prefs.toMutablePreferences();
            mutable.clear();
            return Single.just(mutable);
        }).subscribe();
    }
}
