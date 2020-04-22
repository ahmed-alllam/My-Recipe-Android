/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 15:36
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesRecyclerAdapter;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRecipesFragment extends BaseListsFragment<RecipeModel> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recipesRecyclerView);
        errorLabel = view.findViewById(R.id.errorLabel);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        adapter = new RecipesRecyclerAdapter(getActivity(), this, recyclerView) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if ((holder instanceof RecipeViewHolder)) {
                    RecipesRecyclerAdapter.RecipeViewHolder viewHolder = (RecipesRecyclerAdapter.RecipeViewHolder) holder;
                    setOnFavouriteButtonPressed(viewHolder, position, this, view);
                }
            }
        };

        super.onViewCreated(view, savedInstanceState);
    }

    protected void setOnFavouriteButtonPressed(RecipesRecyclerAdapter.RecipeViewHolder holder, int position,
                                               RecipesRecyclerAdapter adapter, View view) {

        RecipeModel recipe = adapter.get(position);

        if (recipe.isFavouritedByUser())
            holder.favourite.setImageResource(R.drawable.favourite2);
        else
            holder.favourite.setImageResource(R.drawable.favourite_border);

        holder.favourite.setOnClickListener(v -> {
            String token = PreferencesManager.getToken(getContext());
            if (token.length() <= 0)
                return;
            token = "Token " + token;

            APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);
            String slug = recipe.getSlug();


            if (!recipe.isFavouritedByUser()) {
                ((ImageButton) v).setImageResource(R.drawable.favourite2);
                recipe.setFavourites_count(recipe.getFavourites_count() + 1);
                recipe.setFavouritedByUser(true);

                APIInterface.addFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
            } else {
                ((ImageButton) v).setImageResource(R.drawable.favourite_border);
                recipe.setFavourites_count(recipe.getFavourites_count() - 1);
                recipe.setFavouritedByUser(false);

                APIInterface.removeFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
            }

            for (Fragment f : ((MainActivity) getActivity()).getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }
            }

            holder.favourites_count.setText(String.valueOf(recipe.getFavourites_count()));
        });
    }

    protected class emptyCallBack implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    }
}
