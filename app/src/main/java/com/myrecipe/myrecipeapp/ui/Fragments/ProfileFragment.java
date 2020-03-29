/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 21:32
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.ui.Activities.LoginActivity;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.loginLabel).setOnClickListener(v -> {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        });

        view.findViewById(R.id.myRecipesButton).setOnClickListener(v -> {
            MyRecipesFragment myRecipesFragment = new MyRecipesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.main, myRecipesFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
