/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 23:47
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

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.FavouritesModelView;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesRecyclerAdapter;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnRecipeDataChangedListener;


public class FavouritesFragment extends BaseRecipesFragment implements OnRecipeDataChangedListener {

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

        favouritesModelView.favouriteRecipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        favouritesModelView.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    protected void callViewModel(int offset) {
        String username = PreferencesManager.getPreference(getContext(), "user_username", "");
        favouritesModelView.getFavouriteRecipes(getContext(), username, limitPerRequest, offset);
    }

    @Override
    protected void setOnFavouriteButtonPressed(RecipesRecyclerAdapter.RecipeViewHolder holder, int position,
                                               RecipesRecyclerAdapter adapter, View view) {

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

            for (Fragment f : getActivity().getSupportFragmentManager().getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }
            }

            APIInterface.removeFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
        });
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        if (!recipe.isFavouritedByUser())
            adapter.removeRecipe(recipe.getSlug());
        else
            adapter.updateRecipe(recipe);
    }
}
