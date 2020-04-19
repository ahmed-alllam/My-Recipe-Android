/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 19/04/20 22:04
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.MyUserViewModel;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.LoginActivity;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnUserProfileChangedListener;


public class ProfileFragment extends Fragment implements OnUserProfileChangedListener {

    private static final int LAUNCH_LOGIN_ACTIVITY = 0;

    private MyUserViewModel myUserViewModel;
    private UserModel storedUserModel;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myUserViewModel = new ViewModelProvider(this).get(MyUserViewModel.class);

        token = PreferencesManager.getToken(getContext());
        if (token.length() > 0) {
            storedUserModel = getStoredUserInfo();
            refreshUserUIINfo(view, storedUserModel);
            myUserViewModel.getMyProfile(token);
        }

        view.findViewById(R.id.loginLabel).setOnClickListener(v -> {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(loginIntent, LAUNCH_LOGIN_ACTIVITY);
        });

        view.findViewById(R.id.myRecipesButton).setOnClickListener(v -> launchFragment(new MyRecipesFragment()));

        view.findViewById(R.id.settingsButton).setOnClickListener(v -> launchFragment(new SettingsFragment()));

        SwipeRefreshLayout profileFragmentSwipe = view.findViewById(R.id.profileFragmentSwipe);

        int primaryColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        profileFragmentSwipe.setColorSchemeColors(primaryColor, Color.YELLOW, Color.GREEN);

        profileFragmentSwipe.setOnRefreshListener(() -> {
            token = PreferencesManager.getToken(getContext());
            if (token.length() > 0) {
                myUserViewModel.getMyProfile(token);
            } else
                profileFragmentSwipe.setRefreshing(false);
        });

        myUserViewModel.userProfile.observe(getViewLifecycleOwner(), userProfile -> {
            profileFragmentSwipe.setRefreshing(false);

            if (storedUserModel == null || !storedUserModel.sameUserData(userProfile)) {
                refreshUserUIINfo(view, userProfile);
                storeUserInfo(userProfile);

                for (Fragment f : ((MainActivity) getActivity()).getFragments()) {
                    if (f instanceof OnUserProfileChangedListener && f != this) {
                        ((OnUserProfileChangedListener) f).onUserProfileChanged(userProfile, true);
                    }
                }
            }
        });
        myUserViewModel.error.observe(getViewLifecycleOwner(), error -> {
            profileFragmentSwipe.setRefreshing(false);

            if (storedUserModel == null) {
                view.findViewById(R.id.loginLabel).setVisibility(View.INVISIBLE);

                TextView name = view.findViewById(R.id.name);
                name.setVisibility(View.VISIBLE);
                name.setText(error);
            }
        });

        View.OnClickListener onRelatedUsersClicked = v -> {
            int type = -1;
            switch (v.getId()) {
                case R.id.followers:
                    type = RelatedUsersFragment.FOLLOWERS_TYPE;
                    break;
                case R.id.followings:
                    type = RelatedUsersFragment.FOLLOWINGS_TYPE;
            }

            launchFragment(new RelatedUsersFragment(type, storedUserModel.getUsername()));
        };

        view.findViewById(R.id.followings).setOnClickListener(onRelatedUsersClicked);
        view.findViewById(R.id.followers).setOnClickListener(onRelatedUsersClicked);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_LOGIN_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String token = data.getStringExtra("token");
                    myUserViewModel.getMyProfile(token);

                    getView().findViewById(R.id.loginLabel).setVisibility(View.INVISIBLE);

                    TextView name = getView().findViewById(R.id.name);
                    name.setVisibility(View.VISIBLE);
                    name.setText(R.string.loading);
                }
            }
        }
    }

    private void refreshUserUIINfo(View view, UserModel user) {
        ImageView imageView = view.findViewById(R.id.profilePhoto);
        TextView loginLabel = view.findViewById(R.id.loginLabel);
        TextView name = view.findViewById(R.id.name);
        TextView followersNumber = view.findViewById(R.id.followersNumber);
        TextView followingsNumber = view.findViewById(R.id.followingsNumber);
        LinearLayout followers = view.findViewById(R.id.followers);
        LinearLayout followings = view.findViewById(R.id.followings);

        if (user != null) {
            Glide.with(getContext())
                    .load(user.getImage())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.user)
                    .into(imageView);

            loginLabel.setVisibility(View.INVISIBLE);

            name.setVisibility(View.VISIBLE);
            name.setText(user.getName());

            followers.setVisibility(View.VISIBLE);
            followersNumber.setText(String.valueOf(user.getFollowers_count()));

            followings.setVisibility(View.VISIBLE);
            followingsNumber.setText(String.valueOf(user.getFollowings_count()));
        } else {
            imageView.setImageResource(R.drawable.user);
            loginLabel.setVisibility(View.VISIBLE);
            name.setVisibility(View.INVISIBLE);
            followers.setVisibility(View.INVISIBLE);
            followings.setVisibility(View.INVISIBLE);
        }
    }

    private UserModel getStoredUserInfo() {
        return PreferencesManager.getStoredUser(getContext());
    }

    private void launchFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .add(getView().getId(), fragment)
                .addToBackStack(null)
                .commit();
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }

    @Override
    public void onUserProfileChanged(UserModel user, boolean isCurrentUser) {
        if (isCurrentUser) {
            storeUserInfo(user);
            refreshUserUIINfo(getView(), user);
        }
    }

    private void storeUserInfo(UserModel user) {
        storedUserModel = user;
        PreferencesManager.storeUser(getContext(), user);
    }
}
