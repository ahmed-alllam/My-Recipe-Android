/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 24/04/20 00:55
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.FavouritesViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.util.PreferencesManager;


public class FavouritesFragment extends BaseRecipesFragment {

    private FavouritesViewModel favouritesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        favouritesViewModel = new ViewModelProvider(this)
                .get(FavouritesViewModel.class);

        favouritesViewModel.favouriteRecipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        favouritesViewModel.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    protected void callViewModel(int offset) {
        String username = PreferencesManager.getPreference(getContext(), "user_username", "");
        favouritesViewModel.getFavouriteRecipes(getContext(), username, limitPerRequest, offset);
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        int position = adapter.indexOf(recipe);

        if (position >= 0) {
            if (!recipe.isFavouritedByUser())
                adapter.remove(position);
            else {
                adapter.update(recipe, position);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }
}
