/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 21/04/20 21:25
 */

package com.myrecipe.myrecipeapp.models;


public class RecipeReviewModel {
    private UserModel user;
    private String slug;
    private String body;
    private String timestamp;
    private int rating;

    public RecipeReviewModel() {
    }

    public RecipeReviewModel(String body, int rating) {
        this.body = body;
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UserModel getUser() {
        return user;
    }

    public String getSlug() {
        return slug;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
