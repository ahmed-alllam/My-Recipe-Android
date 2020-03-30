/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 30/03/20 18:26
 */

package com.myrecipe.myrecipeapp.data;

import com.myrecipe.myrecipeapp.models.RecipesResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface RecipeInterface {

    @GET("recipes/feed/")
    Call<RecipesResultModel> getFeed(@Query("limit") int limit,
                                     @Query("offset") int offset,
                                     @Query("format") String format);
}
