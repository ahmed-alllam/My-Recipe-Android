/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 24/04/20 00:55
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.CallBacks.OnReviewChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.RecipeReviewsViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Fragments.AddReviewFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.BaseListsFragment;
import com.myrecipe.myrecipeapp.util.PreferencesManager;
import com.myrecipe.myrecipeapp.util.TimeParser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class RecipeReviewsAdapter extends BaseRecyclerAdapter<RecipeReviewModel> {
    private static final int VIEW_TYPE_REVIEW = 0;
    private static final int REVIEW_LOADING_ITEM_HEIGHT = 120;

    private RecipeReviewsViewModel viewModel;
    private RecipeModel recipe;

    public RecipeReviewsAdapter(Context context, BaseListsFragment fragment, RecyclerView recyclerView, RecipeModel recipe) {
        super(context, fragment, recyclerView);
        viewModel = new ViewModelProvider(fragment).get(RecipeReviewsViewModel.class);
        this.recipe = recipe;
    }

    @Override
    int getLoadingItemHeight() {
        return REVIEW_LOADING_ITEM_HEIGHT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY)
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_empty_item, parent, false));

        if (viewType == VIEW_TYPE_LOADING)
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false));

        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_REVIEW)
            return;

        RecipeReviewModel review = list.get(position);

        if (review == null)
            return;

        ReviewViewHolder viewHolder = (ReviewViewHolder) holder;

        Glide.with(context)
                .load(review.getUser().getImage())
                .placeholder(R.drawable.user)
                .into(viewHolder.userPhoto);

        viewHolder.userName.setText(review.getUser().getName());
        viewHolder.timeStamp.setText(TimeParser.parseTime(context, review.getTimestamp()));
        viewHolder.body.setText(review.getBody());
        viewHolder.ratingBar.setRating(review.getRating());

        if (review.getUser().getUsername().equals(PreferencesManager.getStoredUser(context).getUsername())) {
            viewHolder.moreButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.moreButton.setVisibility(View.GONE);
        }

        startAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return VIEW_TYPE_EMPTY;
        if ((position == list.size() - 1 && isLoadingMore) || list.get(position) == null)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_REVIEW;
    }

    private void editReview(RecipeReviewModel review) {
        DialogFragment dialog = new AddReviewFragment(recipe, 0, review);
        ((MainActivity) context).addFragment(dialog);
        dialog.show(fragment.getChildFragmentManager(), "AddReviewDialog");
    }

    private void deleteReview(RecipeReviewModel review, int position) {
        viewModel.deleteReview(context, recipe.getSlug(), review.getSlug());

        review.setUser(null);
        recipe.getReviews().remove(review);

        float rating = ((recipe.getRating() * recipe.getReviews_count()) - review.getRating()) / (recipe.getReviews_count() - 1);

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat(".#");
        decimalFormat.setDecimalFormatSymbols(otherSymbols);

        float formattedRating = Float.parseFloat(decimalFormat.format(rating));
        recipe.setRating(Float.isNaN(formattedRating) ? 0 : formattedRating);

        recipe.setReviews_count(recipe.getReviews_count() - 1);

        recipe.setUsersRating(0);
        for (Fragment f : ((MainActivity) context).getFragments()) {
            if (f instanceof OnRecipeDataChangedListener) {
                ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
            }

            if (f instanceof OnReviewChangedListener) {
                ((OnReviewChangedListener) f).onReviewChanged(review);
            }
        }
    }


    private class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userPhoto;
        private TextView userName;
        private TextView timeStamp;
        private TextView body;
        private RatingBar ratingBar;
        private ImageButton moreButton;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userName = itemView.findViewById(R.id.userName);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            body = itemView.findViewById(R.id.body);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            moreButton = itemView.findViewById(R.id.moreButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();

            int position = getLayoutPosition();
            RecipeReviewModel review = list.get(position);

            inflater.inflate(R.menu.review_popup, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.editReview:
                        editReview(review);
                        return true;
                    case R.id.deleteReview:
                        deleteReview(review, position);
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        }
    }
}
