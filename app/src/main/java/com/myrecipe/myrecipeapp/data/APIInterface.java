/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 05/04/20 18:52
 */

package com.myrecipe.myrecipeapp.data;

import com.myrecipe.myrecipeapp.models.RecipesResultModel;
import com.myrecipe.myrecipeapp.models.UserLoginModel;
import com.myrecipe.myrecipeapp.models.UserModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("recipes/feed/?format=json")
    Call<RecipesResultModel> getFeed(@Query("limit") int limit,
                                     @Query("offset") int offset);

    @GET("recipes/feed/?format=json")
    Call<RecipesResultModel> getFeed(@Header("Authorization") String token,
                                     @Query("limit") int limit,
                                     @Query("offset") int offset);

    @POST("recipes/{slug}/favourite/?format=json")
    Call<Void> addFavouriteRecipe(@Header("Authorization") String token,
                                  @Path("slug") String slug);

    @DELETE("recipes/{slug}/favourite/?format=json")
    Call<Void> removeFavouriteRecipe(@Header("Authorization") String token,
                                     @Path("slug") String slug);

    @GET("users/{username}/favourites/?format=json")
    Call<RecipesResultModel> getFavouriteRecipes(@Header("Authorization") String token,
                                                 @Path("username") String username,
                                                 @Query("limit") int limit,
                                                 @Query("offset") int offset);

    @FormUrlEncoded
    @POST("users/token/?format=json")
    Call<UserLoginModel> loginUser(@Field("email") String email,
                                   @Field("password") String password);

    @GET("users/me/?format=json")
    Call<UserModel> getMyProfile(@Header("Authorization") String token);
}
