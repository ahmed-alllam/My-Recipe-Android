/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 23:54
 */

package com.myrecipe.myrecipeapp.data;

import com.myrecipe.myrecipeapp.models.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesClient {
    private static final String BASE_URL = "http://10.0.2.2";
    private RecipeInterface recipeInterface;

    RecipesClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recipeInterface = retrofit.create(RecipeInterface.class);
    }

    Call<List<RecipeModel>> getFeed(int limit, int offset) {
        return recipeInterface.getFeed(limit, offset);
    }
}
