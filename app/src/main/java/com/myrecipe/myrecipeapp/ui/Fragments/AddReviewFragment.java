/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 19:26
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.CallBacks.OnReviewChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.RecipeReviewsViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;


public class AddReviewFragment extends DialogFragment {
    private float rating;
    private RecipeReviewModel review;
    private RecipeModel recipe;

    public AddReviewFragment(RecipeModel recipe, float rating, RecipeReviewModel review) {
        this.recipe = recipe;
        this.rating = rating;
        this.review = review;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_review, null);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar3);
        EditText body = view.findViewById(R.id.body);
        TextView error = view.findViewById(R.id.error);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        RecipeReviewsViewModel viewModel = new ViewModelProvider(this).get(RecipeReviewsViewModel.class);

        viewModel.review.observe(this, recipeReviewModel -> {
            progressBar.setVisibility(View.GONE);

            recipe.getReviews().add(recipeReviewModel);

            int prevRating = 0;
            int isEditing = 0;
            if (review != null) {
                prevRating = review.getRating();
                isEditing = 1;
            }

            recipe.setRating(((recipe.getRating() * recipe.getReviews_count()) - prevRating + recipeReviewModel.getRating()) / (recipe.getReviews_count() + 1 - isEditing)); // upadting rating
            recipe.setUsersRating(recipeReviewModel.getRating());
            for (Fragment f : ((MainActivity) getActivity()).getFragments()) {
                if (f instanceof OnRecipeDataChangedListener) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }

                if (f instanceof OnReviewChangedListener) {
                    ((OnReviewChangedListener) f).onReviewChanged(recipeReviewModel);
                }
            }
            dismiss();
        });
        viewModel.error.observe(this, errorint -> {
            progressBar.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
            error.setText(errorint);
        });


        if (rating != 0)
            ratingBar.setRating(rating);
        else if (review != null)
            ratingBar.setRating(review.getRating());

        if (review != null)
            body.setText(review.getBody());

        builder.setView(view)
                .setPositiveButton(R.string.send, null)
                .setNegativeButton(R.string.cancel, (dialog, id) -> getDialog().cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);
        positiveButton.setOnClickListener(v -> {
            RecipeReviewModel reviewModel = new RecipeReviewModel(body.getText().toString(), (int) ratingBar.getRating());
            if (review == null)
                viewModel.addReview(getContext(), recipe.getSlug(), reviewModel);
            else
                viewModel.editReview(getContext(), recipe.getSlug(), review.getSlug(), reviewModel);
            error.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        });

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (review != null && !body.getText().toString().isEmpty()) {
                if (rating == review.getRating() && body.getText().toString().equals(review.getBody()))
                    positiveButton.setEnabled(false);
                else
                    positiveButton.setEnabled(true);
            }
        });


        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!body.getText().toString().isEmpty()) {
                    if (review != null) {
                        if (ratingBar.getRating() == review.getRating() && body.getText().toString().equals(review.getBody()))
                            positiveButton.setEnabled(false);
                        else
                            positiveButton.setEnabled(true);
                    } else
                        positiveButton.setEnabled(true);
                } else
                    positiveButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((MainActivity) getActivity()).removeFragment(this);
    }
}
