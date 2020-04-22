/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 17:56
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.CallBacks.OnReviewChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.RecipeReviewsViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipeReviewsAdapter;


public class RecipeReviewsFragment extends BaseListsFragment<RecipeReviewModel> implements OnReviewChangedListener {
    private RecipeModel recipe;
    private RecipeReviewsViewModel viewModel;

    RecipeReviewsFragment(RecipeModel recipe) {
        this.recipe = recipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        viewModel = new ViewModelProvider(this)
                .get(RecipeReviewsViewModel.class);

        recyclerView = view.findViewById(R.id.reviewsRecyclerView);
        adapter = new RecipeReviewsAdapter(getContext(), this, recyclerView, recipe);
        errorLabel = view.findViewById(R.id.errorLabel);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        viewModel.reviews.observe(getViewLifecycleOwner(), reviews -> onNewData(reviews.getReviews(), reviews.getCount()));

        viewModel.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    void callViewModel(int position) {
        viewModel.getReviews(getContext(), recipe.getSlug(), limitPerRequest, position);
    }

    @Override
    public void onReviewChanged(RecipeReviewModel review) {
        int position = adapter.indexOf(review);
        if (position >= 0) {
            if (review.getUser() == null)
                adapter.remove(position);
            else
                adapter.update(review, position);
        }
    }
}
