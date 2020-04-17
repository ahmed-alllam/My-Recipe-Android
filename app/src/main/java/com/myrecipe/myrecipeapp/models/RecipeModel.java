/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 17/04/20 15:55
 */

package com.myrecipe.myrecipeapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class RecipeModel {
    private UserModel user;
    private ArrayList<String> tags;
    private String slug;
    private String name;
    private String description;
    private String body;
    private String main_image;
    private int time_to_finish;
    private String timestamp;
    private ArrayList<String> ingredients;
    private ArrayList<String> images;
    private ArrayList<RecipeReviewModel> reviews;
    private int favourites_count;
    private int reviews_count;
    private float rating;
    @SerializedName("is_favourited_by_user")
    private boolean isFavouritedByUser;


    public boolean isFavouritedByUser() {
        return isFavouritedByUser;
    }

    public void setFavouritedByUser(boolean favouritedByUser) {
        isFavouritedByUser = favouritedByUser;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTime_to_finish() {
        return time_to_finish;
    }

    public void setTime_to_finish(int time_to_finish) {
        this.time_to_finish = time_to_finish;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public UserModel getUser() {
        return user;
    }

    public String getSlug() {
        return slug != null ? slug : "";
    }

    public String getMain_image() {
        return main_image;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public float getRating() {
        return rating;
    }

    public ArrayList<RecipeReviewModel> getReviews() {
        return reviews;
    }
}
