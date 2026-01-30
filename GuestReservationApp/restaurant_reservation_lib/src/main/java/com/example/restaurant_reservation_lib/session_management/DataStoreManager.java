package com.example.restaurant_reservation_lib.session_management;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

public class DataStoreManager {
    private static RxDataStore<Preferences> instance;

    public static synchronized RxDataStore<Preferences> getInstance(Context context) {
        if (instance == null) {
            // Init DataStore
            instance = new RxPreferenceDataStoreBuilder(
                    context.getApplicationContext(), "user_session").build();
        }

        return instance;
    }
}
