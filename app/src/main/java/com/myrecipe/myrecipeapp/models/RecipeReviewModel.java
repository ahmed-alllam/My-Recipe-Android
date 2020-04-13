/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 13/04/20 16:57
 */

package com.myrecipe.myrecipeapp.models;


public class RecipeReviewModel {
    private UserModel user;
    private String slug;
    private String body;
    private String timestamp;
    private int rating;


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
