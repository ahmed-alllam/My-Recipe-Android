/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 04/04/20 20:30
 */

package com.myrecipe.myrecipeapp.models;

public class UserModel {

    private String name;
    private String email;
    private String username;
    private String bio;
    private String image;
    private int followers_count;
    private int followings_count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public void setFollowings_count(int followings_count) {
        this.followings_count = followings_count;
    }

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
