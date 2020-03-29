/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 23:54
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.RecipesViewModel;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesFeedRecyclerAdapter;


public class HomeFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView userPhoto = view.findViewById(R.id.userPhoto);
        ViewPager2 mainViewPager = getActivity().findViewById(R.id.mainViewPager);
        userPhoto.setOnClickListener(v -> mainViewPager.setCurrentItem(3));  // goes to profile page

        RecyclerView recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecipesFeedRecyclerAdapter recipesAdapter = new RecipesFeedRecyclerAdapter();
        recipesRecyclerView.setAdapter(recipesAdapter);

        RecipesViewModel recipesViewModel = new ViewModelProvider(this)
                .get(RecipesViewModel.class);
        recipesViewModel.getFeed(25, 0);

        recipesViewModel.recipes.observe(getViewLifecycleOwner(), recipesAdapter::add);
        recipesViewModel.error.observe(getViewLifecycleOwner(), s -> {
            // todo
        });

        // todo add pagignation

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
