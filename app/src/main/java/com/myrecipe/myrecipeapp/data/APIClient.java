/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 03/04/20 21:32
 */

package com.myrecipe.myrecipeapp.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class APIClient {
    private static final String BASE_URL = "http://192.168.1.8";

    static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
