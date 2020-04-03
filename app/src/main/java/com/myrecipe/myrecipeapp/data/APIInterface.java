/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 03/04/20 21:32
 */

package com.myrecipe.myrecipeapp.data;

import com.myrecipe.myrecipeapp.models.RecipesResultModel;
import com.myrecipe.myrecipeapp.models.UserLoginModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface APIInterface {

    @GET("recipes/feed/?format=json")
    Call<RecipesResultModel> getFeed(@Query("limit") int limit,
                                     @Query("offset") int offset);

    @FormUrlEncoded
    @POST("users/token/?format=json")
    Call<UserLoginModel> loginUser(@Field("email") String email,
                                   @Field("password") String password);
}
