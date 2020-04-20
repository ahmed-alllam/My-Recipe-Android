/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 16:53
 */

package com.myrecipe.myrecipeapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReviewsResultModel {
    @SerializedName("results")
    private List<RecipeReviewModel> reviews = new ArrayList<>();
    private int count;

    public List<RecipeReviewModel> getReviews() {
        return reviews;
    }

    public int getCount() {
        return count;
    }
}
