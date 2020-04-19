/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 19/04/20 22:04
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
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.UsersRecipesViewModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnUserProfileChangedListener;


public class GeneralUsersProfileFragment extends BaseRecipesFragment implements OnUserProfileChangedListener {
    private UsersRecipesViewModel recipesViewModel;
    private UserModel user;

    public GeneralUsersProfileFragment(UserModel user) {
        this.user = user;
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

        recipesViewModel.recipes.observe(getViewLifecycleOwner(), recipesResultModel -> onNewData(recipesResultModel.getRecipes(), recipesResultModel.getCount()));
        recipesViewModel.error.observe(getViewLifecycleOwner(), super::onError);

        view.findViewById(R.id.followUser).setOnClickListener(v -> {
            UserModel me = PreferencesManager.getStoredUser(getContext());

            if (user == null) return;

            String username = user.getUsername();

            String token = PreferencesManager.getToken(getContext());
            if (token.length() <= 0 || username.length() <= 0) {
                return;
            }
            token = "Token " + token;

            APIInterface recipesAPIInterface = APIClient.getClient().create(APIInterface.class);

            if (!user.isFollowedByUser()) {
                recipesAPIInterface.followUser(token, username).enqueue(new emptyCallBack());
                ((Button) v).setText(R.string.unfollow);
                user.setFollowedByUser(true);
                user.setFollowers_count(user.getFollowers_count() + 1);
                me.setFollowings_count(me.getFollowings_count() + 1);
            } else {
                recipesAPIInterface.unFollowUser(token, username).enqueue(new emptyCallBack());
                ((Button) v).setText(R.string.follow);
                user.setFollowedByUser(false);
                user.setFollowers_count(user.getFollowers_count() - 1);
                me.setFollowings_count(me.getFollowings_count() - 1);
            }

            for (Fragment f : ((MainActivity) getActivity()).getFragments()) {
                if (f instanceof OnUserProfileChangedListener && f != this) {
                    ((OnUserProfileChangedListener) f).onUserProfileChanged(me, true);
                    ((OnUserProfileChangedListener) f).onUserProfileChanged(user, false);
                }
            }
        });

        view.findViewById(R.id.followers).setOnClickListener(v -> {
            launchFragment(new RelatedUsersFragment(RelatedUsersFragment.FOLLOWERS_TYPE, user.getUsername()));
        });

        view.findViewById(R.id.followings).setOnClickListener(v -> {
            launchFragment(new RelatedUsersFragment(RelatedUsersFragment.FOLLOWINGS_TYPE, user.getUsername()));
        });

        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        refreshUserData();

        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshUserData() {
        getView().findViewById(R.id.followers).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.followings).setVisibility(View.VISIBLE);

        TextView name = getView().findViewById(R.id.name);
        name.setText(user.getName());

        TextView followersNumber = getView().findViewById(R.id.followersNumber);
        followersNumber.setText(String.valueOf(user.getFollowers_count()));

        TextView followingsNumber = getView().findViewById(R.id.followingsNumber);
        followingsNumber.setText(String.valueOf(user.getFollowings_count()));

        Button followUser = getView().findViewById(R.id.followUser);

        UserModel me = PreferencesManager.getStoredUser(getContext());
        if (!me.getUsername().isEmpty() && !me.getUsername().equals(user.getUsername())) {
            followUser.setVisibility(View.VISIBLE);

            if (user.isFollowedByUser())
                followUser.setText(R.string.unfollow);
            else
                followUser.setText(R.string.follow);
        } else {
            followUser.setVisibility(View.INVISIBLE);
        }

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
        recipesViewModel.getUsersRecipes(getContext(), user.getUsername(), limitPerRequest, position);
    }

    @Override
    public void onUserProfileChanged(UserModel user, boolean isCurrentUser) {
        this.user = user;
        refreshUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }
}
