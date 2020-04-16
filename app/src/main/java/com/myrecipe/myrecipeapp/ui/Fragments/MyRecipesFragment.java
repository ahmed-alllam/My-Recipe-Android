/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 23:47
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.UsersRecipesModelView;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnRecipeDataChangedListener;

public class MyRecipesFragment extends BaseRecipesFragment implements OnRecipeDataChangedListener {

    private UsersRecipesModelView usersRecipesModelView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        usersRecipesModelView = new ViewModelProvider(this)
                .get(UsersRecipesModelView.class);
        usersRecipesModelView.recipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        usersRecipesModelView.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void callViewModel(int offset) {
        UserModel user = PreferencesManager.getStoredUser(getContext());
        usersRecipesModelView.getUsersRecipes(getContext(), user.getUsername(),
                limitPerRequest, offset);
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        adapter.updateRecipe(recipe);
    }
}
