/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 18:08
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.myrecipe.myrecipeapp.R;

public class MyRecipesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(this)
                    .commit();
        });

        TextView noRecipesLabel = view.findViewById(R.id.noRecipesLabel);
        noRecipesLabel.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.main, new MyRecipesFragment())
                    .commit();
        });
    }
}
