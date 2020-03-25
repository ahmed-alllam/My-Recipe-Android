/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 25/03/20 19:53
 */

package com.koshkie.myrecipeapp.models;

public class UserModel {
    private String email;
    private String username;
    private String bio;
    private String image;

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
}
