/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 02/04/20 01:16
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesPaginationScrollListener;


public class HomeFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView userPhoto = view.findViewById(R.id.userPhoto);
        ViewPager2 mainViewPager = getActivity().findViewById(R.id.mainViewPager);
        userPhoto.setOnClickListener(v -> mainViewPager.setCurrentItem(3));

        RecyclerView recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesRecyclerView.setNestedScrollingEnabled(false);

        RecipesFeedRecyclerAdapter recipesAdapter = new RecipesFeedRecyclerAdapter(getContext());
        recipesRecyclerView.setAdapter(recipesAdapter);

        RecipesViewModel recipesViewModel = new ViewModelProvider(this)
                .get(RecipesViewModel.class);

        TextView errorLabel = view.findViewById(R.id.errorLabel);

        recipesRecyclerView.addOnScrollListener(new RecipesPaginationScrollListener(layoutManager) {
            @Override
            public void loadMoreRecipes() {
                recipesAdapter.setLoading(true);

                recipesAdapter.addLoadingFooter();
                recipesViewModel.recipes.setValue(null);

                recipesViewModel.getFeed(3, recipesAdapter.getOffset());

                recipesViewModel.recipes.observe(getViewLifecycleOwner(), (recipes) -> {
                    if (recipes != null) {
                        recipesAdapter.setOffset(recipesAdapter.getOffset() + 3);
                        recipesAdapter.removeLoadingFooter();
                        recipesAdapter.setLoading(false);
                        recipesAdapter.addAll(recipes);
                    }
                });
                recipesViewModel.error.observe(getViewLifecycleOwner(), (error) -> {
                    recipesAdapter.setLoading(false);
                    recipesRecyclerView.setVisibility(View.GONE);
                    errorLabel.setVisibility(View.VISIBLE);
                    errorLabel.setText(error);
                    // todo
                });
            }

            @Override
            public boolean isLastPage() {
                return recipesAdapter.isLastPage();
            }

            @Override
            public boolean isLoading() {
                return recipesAdapter.isLoading();
            }
        });

        recipesAdapter.setLoading(true);
        recipesViewModel.getFeed(3, 0);

        recipesViewModel.recipes.observe(getViewLifecycleOwner(), (recipes) -> {
            recipesAdapter.setOffset(3);
            recipesAdapter.setLoading(false);
            errorLabel.setVisibility(View.GONE);
            recipesRecyclerView.setVisibility(View.VISIBLE);
            recipesAdapter.addAll(recipes);
            recipesRecyclerView.setNestedScrollingEnabled(true);

            recipesViewModel.recipes.removeObservers(getViewLifecycleOwner());
        });
        recipesViewModel.error.observe(getViewLifecycleOwner(), (error) -> {
            recipesAdapter.setLoading(false);
            recipesRecyclerView.setVisibility(View.GONE);
            errorLabel.setVisibility(View.VISIBLE);
            errorLabel.setText(error);
        });
        recipesViewModel.count.observe(getViewLifecycleOwner(), recipesAdapter::setCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
