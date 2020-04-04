/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 04/04/20 20:30
 */

package com.myrecipe.myrecipeapp.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class APIClient {
    private static final String BASE_URL = "http://192.168.1.2";
    private static Retrofit retrofit;

    static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
