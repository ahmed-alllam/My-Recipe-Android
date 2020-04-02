/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 02/04/20 21:12
 */

package com.myrecipe.myrecipeapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipesResultModel {

    @SerializedName("results")
    private List<RecipeModel> recipes = new ArrayList<>();
    private int count;

    public List<RecipeModel> getRecipes() {
        return recipes;
    }

    public int getCount() {
        return count;
    }
}
