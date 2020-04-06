/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 06/04/20 21:09
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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

        recyclerView = view.findViewById(R.id.favouritesRecyclerView);

        favouritesModelView = new ViewModelProvider(this)
                .get(FavouritesModelView.class);

        recipes = favouritesModelView.favouriteRecipes;
        error = favouritesModelView.error;

        super.onViewCreated(view, savedInstanceState);

        // todo: solve pagination after favourite problem
    }

    protected void callModelView(int offset) {
        String username = PreferencesManager.getPreference(getContext(), "user_username", "");
        favouritesModelView.getFavouriteRecipes(getContext(), username, limitPerRequest, offset);
    }

    @Override
    protected void setOnFavouriteButtonPressed(RecyclerView.ViewHolder holder, int position,
                                               BaseRecipesAdapter adapter, View view) {

        RecipeModel recipe = adapter.getRecipesList().get(position);

        BaseRecipesAdapter.RecipeViewHolder viewHolder = (BaseRecipesAdapter.RecipeViewHolder) holder;

        viewHolder.favourite.setImageResource(R.drawable.favourite2);

        viewHolder.favourite.setOnClickListener(v -> {
            String token = PreferencesManager.getToken(recyclerView.getContext());
            if (token.length() <= 0)
                return;
            token = "Token " + token;

            APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);
            String slug = recipe.getSlug();

            recipe.setFavouritedByUser(false);
            recipe.setFavourites_count(recipe.getFavourites_count() - 1);

            adapter.removeRecipe(position);

            recyclerView.post(() -> {
                HomeFragment homeFragment = null;
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof HomeFragment)
                        homeFragment = (HomeFragment) fragment;
                }
                if (homeFragment != null)
                    ((BaseRecipesAdapter) homeFragment.recyclerView.getAdapter()).updateRecipe(slug, recipe);
            });

            APIInterface.removeFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
        });
    }
}
