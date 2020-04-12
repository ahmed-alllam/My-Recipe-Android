/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 12/04/20 22:50
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
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.RecipesFeedViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecipesAdapter;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnRecipeDataChangedListener;


public class HomeFragment extends BaseRecipesFragment implements OnRecipeDataChangedListener {

    private RecipesFeedViewModel recipesFeedViewModel;

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

        recipesFeedViewModel = new ViewModelProvider(this)
                .get(RecipesFeedViewModel.class);
        recipes = recipesFeedViewModel.recipes;
        error = recipesFeedViewModel.error;

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void callModelView(int offset) {
        recipesFeedViewModel.getFeed(getContext(), limitPerRequest, offset);
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
    protected void setOnFavouriteButtonPressed(BaseRecipesAdapter.RecipeViewHolder holder, int position,
                                               BaseRecipesAdapter adapter, View view) {

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

            for (Fragment f : getActivity().getSupportFragmentManager().getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }
            }

            holder.favourites_count.setText(String.valueOf(recipe.getFavourites_count()));
        });
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        adapter.updateRecipe(recipe);
    }
}
