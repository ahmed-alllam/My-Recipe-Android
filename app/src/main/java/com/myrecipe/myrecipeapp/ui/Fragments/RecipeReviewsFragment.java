/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 19/04/20 23:38
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myrecipe.myrecipeapp.R;


public class RecipeReviewsFragment extends BaseRecipesFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_reviews, container, false);
    }

    @Override
    void callViewModel(int position) {

    }
}
