/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 18:12
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.GeneralUserViewModel;
import com.myrecipe.myrecipeapp.data.UsersRecipesViewModel;
import com.myrecipe.myrecipeapp.models.UserModel;


public class GeneralUsersProfileFragment extends BaseRecipesFragment {
    private UsersRecipesViewModel recipesViewModel;
    private GeneralUserViewModel userViewModel;
    private String username;

    public GeneralUsersProfileFragment(String username) {
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_users_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recipesViewModel = new ViewModelProvider(this)
                .get(UsersRecipesViewModel.class);
        userViewModel = new ViewModelProvider(this)
                .get(GeneralUserViewModel.class);

        ((TextView) view.findViewById(R.id.name)).setText(R.string.loading);

        userViewModel.user.observe(getViewLifecycleOwner(), this::refreshUserData);
        userViewModel.error.observe(getViewLifecycleOwner(), this::onError);

        recipesViewModel.recipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        recipesViewModel.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshUserData(UserModel user) {

    }

    @Override
    void callViewModel(int position) {
        userViewModel.getUserProfile(username);
        recipesViewModel.getUsersRecipes(getContext(), username, limitPerRequest, position);
    }
}
