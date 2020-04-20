/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 16:53
 */

package com.myrecipe.myrecipeapp.data;

import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipesResultModel;
import com.myrecipe.myrecipeapp.models.ReviewsResultModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.models.UserTokenModel;
import com.myrecipe.myrecipeapp.models.UsersResultModel;

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

    @GET("recipes/{slug}/?format=json")
    Call<RecipeModel> getRecipe(@Path("slug") String Slug);

    @GET("recipes/{slug}/?format=json")
    Call<RecipeModel> getRecipe(@Header("Authorization") String token,
                                @Path("slug") String Slug);

    @GET("recipes/{slug}/reviews/?format=json")
    Call<ReviewsResultModel> getRecipeReviews(@Path("slug") String Slug,
                                              @Query("limit") int limit,
                                              @Query("offset") int offset);

    @GET("recipes/{slug}/reviews/?format=json")
    Call<ReviewsResultModel> getRecipeReviews(@Header("Authorization") String token,
                                              @Path("slug") String Slug,
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

    @GET("users/{username}/recipes/?format=json")
    Call<RecipesResultModel> getUsersRecipes(@Path("username") String username,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("users/{username}/recipes/?format=json")
    Call<RecipesResultModel> getUsersRecipes(@Header("Authorization") String token,
                                             @Path("username") String username,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("users/{username}/followers/?format=json")
    Call<UsersResultModel> getUsersFollowers(@Header("Authorization") String token,
                                             @Path("username") String username,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("users/{username}/followers/?format=json")
    Call<UsersResultModel> getUsersFollowers(@Path("username") String username,
                                             @Query("limit") int limit,
                                             @Query("offset") int offset);

    @GET("users/{username}/followings/?format=json")
    Call<UsersResultModel> getUsersFollowings(@Header("Authorization") String token,
                                              @Path("username") String username,
                                              @Query("limit") int limit,
                                              @Query("offset") int offset);

    @GET("users/{username}/followings/?format=json")
    Call<UsersResultModel> getUsersFollowings(@Path("username") String username,
                                              @Query("limit") int limit,
                                              @Query("offset") int offset);

    @FormUrlEncoded
    @POST("users/token/?format=json")
    Call<UserTokenModel> loginUser(@Field("email") String email,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("users/signup/?format=json")
    Call<UserModel> signup(@Field("name") String name,
                           @Field("email") String email,
                           @Field("password") String password);

    @GET("users/me/?format=json")
    Call<UserModel> getMyProfile(@Header("Authorization") String token);

    @POST("users/{username}/follow/")
    Call<Void> followUser(@Header("Authorization") String token,
                          @Path("username") String username);

    @DELETE("users/{username}/follow/")
    Call<Void> unFollowUser(@Header("Authorization") String token,
                            @Path("username") String username);
}
