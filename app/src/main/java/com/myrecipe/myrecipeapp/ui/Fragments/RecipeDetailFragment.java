/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 10/04/20 20:43
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.RecipeDetailViewModel;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecipeDetailViewModel viewModel = new ViewModelProvider(this)
                .get(RecipeDetailViewModel.class);

        viewModel.recipe.observe(getViewLifecycleOwner(), recipeModel -> {
            // todo
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            // todo
        });

        Bundle recipeBundle = getArguments();

        viewModel.getRecipe(recipeBundle.getString("slug"));


        view.findViewById(R.id.backButton).setOnClickListener((v) -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
            getActivity().getSupportFragmentManager().popBackStack();
        });

        ImageView recipeImage = view.findViewById(R.id.recipeImage);
        Glide.with(getContext())
                .load(recipeBundle.getString("main_image"))
                .placeholder(R.drawable.placeholder)
                .into(recipeImage);

        TextView recipeName = view.findViewById(R.id.recipeName);
        recipeName.setText(recipeBundle.getString("name"));

        TextView favouritesCount = view.findViewById(R.id.favouritesCount);
        favouritesCount.setText(String.valueOf(recipeBundle.getInt("favourites_count")));

        TextView timeToFinish = view.findViewById(R.id.timeToFinish);
        timeToFinish.setText(String.format("%s %s", recipeBundle.getInt("time_to_finish"),
                getContext().getResources().getString(R.string.minutes)));

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setRating(recipeBundle.getFloat("rating"));

        TextView recipeDescription = view.findViewById(R.id.recipeDescription);
        recipeDescription.setText(String.valueOf(recipeBundle.getString("description")));

        ImageButton favourite = view.findViewById(R.id.favourite);
        boolean isFavouritedByUser = recipeBundle.getBoolean("isFavouritedByUser");

        if (isFavouritedByUser)
            favourite.setImageResource(R.drawable.favourite2);

        // todo: add onclicklistner for favourite buttton;
    }
}
