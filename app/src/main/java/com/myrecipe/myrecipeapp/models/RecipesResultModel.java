/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 30/03/20 18:26
 */

package com.myrecipe.myrecipeapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipesResultModel {

    @SerializedName("results")
    private List<RecipeModel> recipes;

    public List<RecipeModel> getRecipes() {
        return recipes;
    }
}
