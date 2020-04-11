/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 11/04/20 23:30
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
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.FavouritesModelView;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecipesAdapter;


public class FavouritesFragment extends BaseRecipesFragment {

    private FavouritesModelView favouritesModelView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        favouritesModelView = new ViewModelProvider(this)
                .get(FavouritesModelView.class);

        recipes = favouritesModelView.favouriteRecipes;
        error = favouritesModelView.error;

        super.onViewCreated(view, savedInstanceState);
    }

    protected void callModelView(int offset) {
        String username = PreferencesManager.getPreference(getContext(), "user_username", "");
        favouritesModelView.getFavouriteRecipes(getContext(), username, limitPerRequest, offset);
    }

    @Override
    protected void setOnFavouriteButtonPressed(BaseRecipesAdapter.RecipeViewHolder holder, int position,
                                               BaseRecipesAdapter adapter, View view) {

        RecipeModel recipe = adapter.get(position);


        holder.favourite.setImageResource(R.drawable.favourite2);

        holder.favourite.setOnClickListener(v -> {
            String token = PreferencesManager.getToken(getContext());
            if (token.length() <= 0)
                return;
            token = "Token " + token;

            APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);
            String slug = recipe.getSlug();

            recipe.setFavouritedByUser(false);
            recipe.setFavourites_count(recipe.getFavourites_count() - 1);

            adapter.removeRecipe(slug);


            APIInterface.removeFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
        });
    }
}
