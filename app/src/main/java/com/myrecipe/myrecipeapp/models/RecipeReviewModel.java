/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 21:32
 */

package com.myrecipe.myrecipeapp.models;


public class RecipeReviewModel {
    private UserModel user;
    private String title;
    private String slug;
    private String body;
    private String timestamp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
