/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 23:42
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.GeneralUserViewModel;
import com.myrecipe.myrecipeapp.data.UsersRecipesViewModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;


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


        userViewModel.user.observe(getViewLifecycleOwner(), this::refreshUserData);
        userViewModel.error.observe(getViewLifecycleOwner(), this::onError);

        recipesViewModel.recipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        recipesViewModel.error.observe(getViewLifecycleOwner(), super::onError);

        view.findViewById(R.id.followUser).setOnClickListener(v -> {
            // todo
        });

        view.findViewById(R.id.followers).setOnClickListener(v -> {
            launchFragment(new RelatedUsersFragment(RelatedUsersFragment.FOLLOWERS_TYPE, username));
        });

        view.findViewById(R.id.followings).setOnClickListener(v -> {
            launchFragment(new RelatedUsersFragment(RelatedUsersFragment.FOLLOWINGS_TYPE, username));
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshUserData(UserModel user) {
        getView().findViewById(R.id.followers).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.followings).setVisibility(View.VISIBLE);

        TextView name = getView().findViewById(R.id.name);
        name.setText(user.getName());

        TextView followersNumber = getView().findViewById(R.id.followersNumber);
        followersNumber.setText(String.valueOf(user.getFollowers_count()));

        TextView followingsNumber = getView().findViewById(R.id.followingsNumber);
        followingsNumber.setText(String.valueOf(user.getFollowings_count()));

        Button followUser = getView().findViewById(R.id.followUser);
        followUser.setVisibility(View.VISIBLE);

        if (user.isFollowedByUser())
            followUser.setText(R.string.unfollow);
        else
            followUser.setText(R.string.follow);

        ImageView profilePhoto = getView().findViewById(R.id.profilePhoto);
        Glide.with(getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user)
                .into(profilePhoto);
    }

    private void launchFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(getView().getId(), fragment)
                .addToBackStack(null)
                .commit();
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    @Override
    void callViewModel(int position) {
        userViewModel.getUserProfile(username);
        recipesViewModel.getUsersRecipes(getContext(), username, limitPerRequest, position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).removeFragment(this);
    }

    @Override
    void onError(int error) {
        super.onError(error);
        ((TextView) getView().findViewById(R.id.name)).setText(R.string.profile_network_error);
    }
}
