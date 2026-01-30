package com.example.restaurant_reservation_lib.session_management;

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

public class SessionManager extends AppCompatActivity {
    // DataStore for secure preferences
    // Used to store key-value pairs in a file
    private RxDataStore<Preferences> dataStore;
    private ExecutorService executorService;

    // DataStore keys for tokens
    private static final Preferences.Key<String> KEY_ACCESS_TOKEN = PreferencesKeys.stringKey("access_token");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Init DataStore
        dataStore = DataStoreManager.getInstance(this);
    }

    // Save encrypted token in DataStore
    protected void saveToken(String accessToken, Runnable onComplete) {
        executorService.execute(() -> {
            // Encrypt tokens and user id
            String encryptedAccessToken = Encryptor.encrypt(accessToken);

            // Store in DataStore
            // updateDataAsync(): ensure thread-safe, atomic changes
            Single<Preferences> updateSingle = dataStore.updateDataAsync(prefs -> {
                MutablePreferences mutable = prefs.toMutablePreferences();
                mutable.set(KEY_ACCESS_TOKEN, encryptedAccessToken);
                // Store user id too
                return Single.just(mutable);
            });
            updateSingle.subscribe();
            new Handler(Looper.getMainLooper()).post(onComplete);
        });

        Log.d("saveTokens", "Token saved in DataStore");
    }

    // Retrieve and decrypt access token
    public String getAccessToken() {
        Preferences prefs = dataStore.data().blockingFirst();
        String encryptedToken = prefs.get(KEY_ACCESS_TOKEN);

        return encryptedToken != null ? Encryptor.decrypt(encryptedToken) : null;
    }

    // Clear token
    protected void clearToken() {
        Single<Preferences> updateSingle = dataStore.updateDataAsync(prefs -> {
            MutablePreferences mutable = prefs.toMutablePreferences();
            mutable.clear();
            return Single.just(mutable);
        });
        updateSingle.subscribe(
                preferences -> Log.d("SessionManager", "Token cleared successfully"),
                throwable -> Log.e("SessionManager", "Failed to clear token: " + throwable.getMessage())
        );
    }
}
