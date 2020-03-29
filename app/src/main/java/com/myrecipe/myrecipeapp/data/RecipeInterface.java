/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 23:54
 */

package com.myrecipe.myrecipeapp.data;

import com.myrecipe.myrecipeapp.models.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeInterface {

    @GET("recipes/feed")
    Call<List<RecipeModel>> getFeed(@Query("limit") int limit,
                                    @Query("offset") int offset);
}
