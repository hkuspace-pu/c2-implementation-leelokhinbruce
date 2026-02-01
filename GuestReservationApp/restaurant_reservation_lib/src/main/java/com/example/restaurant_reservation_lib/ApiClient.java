package com.example.restaurant_reservation_lib;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient{

    public static Retrofit getClient(String token) {
        // OKHttp Interceptor
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))  // Pass token to header
                .build();

        return new Retrofit.Builder()
                    .client(client)
                    .baseUrl("http://192.168.68.51:8080/")  // Connected website (e.g. http://192.168.50.246:8080/)
                    // addConverterFactory(): uses Gson as info handling Converte
                    .addConverterFactory(ScalarsConverterFactory.create())  // handles plain strings
                    .addConverterFactory(GsonConverterFactory.create())  // Change to Gson info
                    .build();
    }

    // Retrofit Interceptor for Token (Automatic Header Addition)
    public static class AuthInterceptor implements Interceptor {
        private final String token;

        public AuthInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            if (token != null) {
                Request modified = original.newBuilder()
                        // add "Bearer token" to every request of header automatically
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(modified);
            }

            // No token request -> skip adding to header (for public endpoint)
            return chain.proceed(original);
        }
    }
}
