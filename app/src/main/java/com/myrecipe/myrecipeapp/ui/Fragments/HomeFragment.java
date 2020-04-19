/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 19/04/20 18:28
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
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.RecipesFeedViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesRecyclerAdapter;
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

        recipesFeedViewModel.recipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        recipesFeedViewModel.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void callViewModel(int offset) {
        recipesFeedViewModel.getFeed(getContext(), limitPerRequest, offset);
    }

    private void refreshUserImage(String image) {
        ImageView userImage = getView().findViewById(R.id.userImage);
        Glide.with(getContext())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_icon)
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
        ((RecipesRecyclerAdapter) adapter).updateRecipe(recipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }
}
