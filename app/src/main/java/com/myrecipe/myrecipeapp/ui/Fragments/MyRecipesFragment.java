/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 15:36
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

import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.UsersRecipesViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesRecyclerAdapter;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

public class MyRecipesFragment extends BaseRecipesFragment implements OnRecipeDataChangedListener {

    private UsersRecipesViewModel usersRecipesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        usersRecipesViewModel = new ViewModelProvider(this)
                .get(UsersRecipesViewModel.class);
        usersRecipesViewModel.recipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        usersRecipesViewModel.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void callViewModel(int offset) {
        UserModel user = PreferencesManager.getStoredUser(getContext());
        usersRecipesViewModel.getUsersRecipes(getContext(), user.getUsername(),
                limitPerRequest, offset);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        ((RecipesRecyclerAdapter) adapter).updateRecipe(recipe);
    }
}
