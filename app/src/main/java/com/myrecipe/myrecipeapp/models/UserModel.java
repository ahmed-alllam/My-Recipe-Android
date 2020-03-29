/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 21:32
 */

package com.myrecipe.myrecipeapp.models;

public class UserModel {
    private String email;
    private String username;
    private String bio;
    private String image;
    private int followers_count;
    private int followings_count;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFollowings_count() {
        return followings_count;
    }
}
