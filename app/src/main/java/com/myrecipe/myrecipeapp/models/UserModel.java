/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 19/04/20 22:04
 */

package com.myrecipe.myrecipeapp.models;

public class UserModel {

    private String name;
    private String email;
    private String password;
    private String username;
    private String bio;
    private String image;
    private int followers_count;
    private int followings_count;
    private boolean isFollowedByUser;

    public UserModel() {
    }

    public UserModel(String name, String email, String username, String bio, String image, int followers_count, int followings_count) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.followers_count = followers_count;
        this.followings_count = followings_count;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFollowedByUser() {
        return isFollowedByUser;
    }

    public void setFollowedByUser(boolean followedByUser) {
        isFollowedByUser = followedByUser;
    }

    public String getName() {
        return name != null ? name : "";
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
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username != null ? username : "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio != null ? bio : "";
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image != null ? image : "";
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

    @Override
    public boolean equals(Object userModel) {
        if (!(userModel instanceof UserModel)) return false;

        UserModel user = (UserModel) userModel;

        return user.getUsername().equals(getUsername());
    }

    public boolean sameUserData(UserModel userModel) {
        if (userModel == null) return false;

        return userModel.getName().equals(getName()) &&
                userModel.getEmail().equals(getEmail()) &&
                userModel.getUsername().equals(getUsername()) &&
                userModel.getBio().equals(getBio()) &&
                userModel.getImage().equals(getImage()) &&
                userModel.getFollowers_count() == getFollowers_count() &&
                userModel.getFollowings_count() == getFollowings_count();
    }
}
