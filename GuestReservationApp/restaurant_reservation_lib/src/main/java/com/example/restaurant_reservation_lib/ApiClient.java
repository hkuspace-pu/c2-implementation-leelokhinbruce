package com.example.restaurant_reservation_lib;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.50.246:8080/")  // Connected website
                    // addConverterFactory(): uses Gson as info handling Converte
                    .addConverterFactory(GsonConverterFactory.create())  // Change to Gson info
                    .build();
        }

        return retrofit;
    }
}
