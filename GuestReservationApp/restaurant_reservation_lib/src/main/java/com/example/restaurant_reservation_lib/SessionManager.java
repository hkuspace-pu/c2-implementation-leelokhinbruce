package com.example.restaurant_reservation_lib;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Single;
import io.reactivex.functions.Function;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.rxjava2.RxDataStore;

public class SessionManager extends AppCompatActivity {
    // Save encrypted token in DataStore
    protected void saveToken(String accessToken, RxDataStore<Preferences> dataStore,
                              Preferences.Key<String> key_token) {
        // Encrypt tokens
        String encryptedAccessToken = Encryptor.encrypt(accessToken);

        // Store in DataStore
        // updateDataAsync(): ensure thread-safe, atomic changes
        Single<Preferences> updateSingle = dataStore.updateDataAsync(prefs -> {
            MutablePreferences mutable = prefs.toMutablePreferences();
            mutable.set(key_token, encryptedAccessToken);
            return Single.just(mutable);
        });
        updateSingle.subscribe();
        Log.d("saveTokens", "Token saved in DataStore");
    }

    // Retrieve and decrypt access token
    protected String getAccessToken(RxDataStore<Preferences> dataStore,
                                    Preferences.Key<String> key_token) {
        Preferences prefs = dataStore.data().blockingFirst();
        String encryptedToken = prefs.get(key_token);

        return encryptedToken != null ? Encryptor.decrypt(encryptedToken) : null;
    }

    // Clear token
    protected void clearToken(RxDataStore<Preferences> dataStore) {
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
