/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 10/04/20 20:43
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.UserViewModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.LoginActivity;
import com.myrecipe.myrecipeapp.ui.Adapters.MainViewPagerAdapter;


public class ProfileFragment extends Fragment {

    private static final int LAUNCH_LOGIN_ACTIVITY = 0;

    private UserViewModel userViewModel;
    private UserModel storedUserModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        String token = PreferencesManager.getToken(getContext());
        if (token.length() > 0) {
            storedUserModel = getStoredUserInfo();
            refreshUserUIINfo(view, storedUserModel);
            userViewModel.getMyProfile(token);
        }

        view.findViewById(R.id.loginLabel).setOnClickListener(v -> {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(loginIntent, LAUNCH_LOGIN_ACTIVITY);
        });

        view.findViewById(R.id.myRecipesButton).setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(view.getId(), new MyRecipesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        userViewModel.userProfile.observe(getViewLifecycleOwner(), userProfile -> {
            if (storedUserModel == null || !storedUserModel.equals(userProfile)) {
                refreshUserUIINfo(view, userProfile);
                storeUserInfo(userProfile);

                // gets the home fragment instance and calls its refreshUserImage method
                ViewPager2 viewPager = getActivity().findViewById(R.id.mainViewPager);
                MainViewPagerAdapter adapter = (MainViewPagerAdapter) viewPager.getAdapter();
                ((HomeFragment) adapter.createFragment(0)).refreshUserImage(userProfile);
            }
        });
        userViewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (storedUserModel == null) {
                view.findViewById(R.id.loginLabel).setVisibility(View.INVISIBLE);

                TextView name = view.findViewById(R.id.name);
                name.setVisibility(View.VISIBLE);
                name.setText(error);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_LOGIN_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String token = data.getStringExtra("token");
                    userViewModel.getMyProfile(token);

                    getView().findViewById(R.id.loginLabel).setVisibility(View.INVISIBLE);

                    TextView name = getView().findViewById(R.id.name);
                    name.setVisibility(View.VISIBLE);
                    name.setText(R.string.loading);
                }
            }
        }
    }

    private void refreshUserUIINfo(View view, UserModel user) {
        if (user != null) {
            Glide.with(getContext())
                    .load(user.getImage())
                    .placeholder(R.drawable.user)
                    .into((ImageView) view.findViewById(R.id.profilePhoto));

            view.findViewById(R.id.loginLabel).setVisibility(View.INVISIBLE);

            TextView name = view.findViewById(R.id.name);
            name.setVisibility(View.VISIBLE);
            name.setText(user.getName());

            TextView bio = view.findViewById(R.id.bio);
            bio.setVisibility(View.VISIBLE);
            bio.setText(user.getBio());

            TextView followersNumber = view.findViewById(R.id.followersNumber);
            followersNumber.setVisibility(View.VISIBLE);
            followersNumber.setText(String.valueOf(user.getFollowers_count()));
            view.findViewById(R.id.followersLabel).setVisibility(View.VISIBLE);

            TextView followingsNumber = view.findViewById(R.id.followingsNumber);
            followingsNumber.setVisibility(View.VISIBLE);
            followingsNumber.setText(String.valueOf(user.getFollowings_count()));
            view.findViewById(R.id.followingsLabel).setVisibility(View.VISIBLE);
        }
    }

    private UserModel getStoredUserInfo() {
        return PreferencesManager.getStoredUser(getContext());
    }

    private void storeUserInfo(UserModel user) {
        storedUserModel = user;
        PreferencesManager.storeUser(getContext(), user);
    }
}
