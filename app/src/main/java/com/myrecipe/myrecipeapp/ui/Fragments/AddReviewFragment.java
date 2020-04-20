/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 22:59
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;


public class AddReviewFragment extends DialogFragment {
    private String recipeSlug;
    private float rating;
    private RecipeReviewModel review;
    private RatingBar parentsRatingBar;
    private boolean sent;

    AddReviewFragment(String recipeSlug, float rating, RecipeReviewModel review) {
        this.recipeSlug = recipeSlug;
        this.rating = rating;
        this.review = review;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_review, null);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar3);
        ratingBar.setRating(rating);

        parentsRatingBar = getParentFragment().getView().findViewById(R.id.ratingBar2);

        builder.setView(view)
                .setPositiveButton(R.string.send, (dialog, id) -> {
                    sent = true;
                    parentsRatingBar.setIsIndicator(true);
                    parentsRatingBar.setRating(ratingBar.getRating());

                    // todo add sending to server
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> getDialog().cancel());
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (!sent)
            parentsRatingBar.setRating(0);
        ((MainActivity) getActivity()).removeFragment(this);
    }
}
