/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 18:48
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.RecipesFeedViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnUserProfileChangedListener;


public class HomeFragment extends BaseRecipesFragment implements OnRecipeDataChangedListener, OnUserProfileChangedListener {

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
        refreshUserImage(PreferencesManager.getStoredUser(getContext()).getImage());

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

    private void refreshUserImage(String image) {
        ImageView userImage = getView().findViewById(R.id.userImage);
        Glide.with(getContext())
                .load(image)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.user)
                .into(userImage);
    }

    @Override
    public void onUserProfileChanged(UserModel user) {
        String image;
        if (user != null)
            image = user.getImage();
        else
            image = "";
        refreshUserImage(image);
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        adapter.updateRecipe(recipe);
    }
}
