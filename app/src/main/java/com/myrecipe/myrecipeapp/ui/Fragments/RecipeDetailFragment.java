/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 08/04/20 17:10
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeModel;


public class RecipeDetailFragment extends Fragment {

    static RecipeDetailFragment newInstance(RecipeModel recipe) {
        RecipeDetailFragment recipeFragment = new RecipeDetailFragment();

        Bundle recipeArgs = new Bundle();
        recipeArgs.putString("slug", recipe.getSlug());
        recipeArgs.putStringArrayList("tags", recipe.getTags());
        recipeArgs.putString("name", recipe.getName());
        recipeArgs.putString("description", recipe.getDescription());
        recipeArgs.putString("main_image", recipe.getMain_image());
        recipeArgs.putInt("time_to_finish", recipe.getTime_to_finish());
        recipeArgs.putInt("favourites_count", recipe.getFavourites_count());
        recipeArgs.putFloat("rating", recipe.getRating());
        recipeArgs.putBoolean("isFavouritedByUser", recipe.isFavouritedByUser());

        recipeFragment.setArguments(recipeArgs);

        return recipeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
