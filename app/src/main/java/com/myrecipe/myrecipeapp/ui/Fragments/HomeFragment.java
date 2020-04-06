/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 07/04/20 00:55
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.RecipesViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecipesAdapter;


public class HomeFragment extends BaseRecipesFragment {

    private RecipesViewModel recipesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ImageView userImage = view.findViewById(R.id.userImage);
        ViewPager2 mainViewPager = getActivity().findViewById(R.id.mainViewPager);
        userImage.setOnClickListener(v -> mainViewPager.setCurrentItem(3));
        refreshUserImage(PreferencesManager.getStoredUser(getContext()));

        recyclerView = view.findViewById(R.id.recipesRecyclerView);

        recipesViewModel = new ViewModelProvider(this)
                .get(RecipesViewModel.class);
        recipes = recipesViewModel.recipes;
        error = recipesViewModel.error;

        super.onViewCreated(view, savedInstanceState);
        // todo: solve pagination after login problem
    }


    @Override
    protected void callModelView(int offset) {
        recipesViewModel.getFeed(getContext(), limitPerRequest, offset);
    }

    void refreshUserImage(UserModel user) {
        ImageView userImage = getView().findViewById(R.id.userImage);
        if (!user.getImage().isEmpty()) {
            Glide.with(getContext())
                    .load(user.getImage())
                    .placeholder(R.drawable.user)
                    .into(userImage);
        }
    }

    @Override
    protected void setOnFavouriteButtonPressed(RecyclerView.ViewHolder holder, int position,
                                               BaseRecipesAdapter adapter, View view) {

        RecipeModel recipe = adapter.getRecipesList().get(position);
        BaseRecipesAdapter.RecipeViewHolder viewHolder = (BaseRecipesAdapter.RecipeViewHolder) holder;

        if (recipe.isFavouritedByUser())
            viewHolder.favourite.setImageResource(R.drawable.favourite2);
        else
            viewHolder.favourite.setImageResource(R.drawable.favourite_border);

        viewHolder.favourite.setOnClickListener(v -> {
            String token = PreferencesManager.getToken(recyclerView.getContext());
            if (token.length() <= 0)
                return;
            token = "Token " + token;

            APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);
            String slug = recipe.getSlug();

            BaseRecipesAdapter favouriteAdapter = null;
            FavouritesFragment favouritesFragment = null;
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof FavouritesFragment) {
                    favouritesFragment = (FavouritesFragment) fragment;
                    favouriteAdapter = (BaseRecipesAdapter) favouritesFragment.recyclerView.getAdapter();
                }
            }

            if (!recipe.isFavouritedByUser()) {
                ((ImageButton) v).setImageResource(R.drawable.favourite2);
                recipe.setFavourites_count(recipe.getFavourites_count() + 1);
                recipe.setFavouritedByUser(true);

                APIInterface.addFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
            } else {
                ((ImageButton) v).setImageResource(R.drawable.favourite_border);
                recipe.setFavourites_count(recipe.getFavourites_count() - 1);
                recipe.setFavouritedByUser(false);

                favouriteAdapter.removeRecipe(recipe.getSlug());

                APIInterface.removeFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
            }

            viewHolder.favourites_count.setText(String.valueOf(recipe.getFavourites_count()));
        });
    }
}
