/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 23:47
 */

package com.myrecipe.myrecipeapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UsersResultModel {

    @SerializedName("results")
    private List<UserModel> users = new ArrayList<>();
    private int count;

    public List<UserModel> getUsers() {
        return users;
    }

    public int getCount() {
        return count;
    }
}
