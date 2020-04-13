/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 13/04/20 16:57
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.data.RecipeDetailViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnRecipeDataChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeDetailFragment extends Fragment implements OnRecipeDataChangedListener {

    private RecipeModel recipe;

    RecipeDetailFragment(RecipeModel recipe) {
        this.recipe = recipe;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecipeDetailViewModel viewModel = new ViewModelProvider(this)
                .get(RecipeDetailViewModel.class);

        viewModel.recipe.observe(getViewLifecycleOwner(), (recipe) -> {
            this.recipe = recipe;
            refreshRecipeData(view);
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            // todo
        });

        String recipeSlug = recipe.getSlug();

        viewModel.getRecipe(getContext(), recipeSlug);


        view.findViewById(R.id.backButton).setOnClickListener((v) -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
            getActivity().getSupportFragmentManager().popBackStack();
        });

        ImageView recipeImage = view.findViewById(R.id.recipeImage);
        Glide.with(getContext())
                .load(recipe.getMain_image())
                .placeholder(R.drawable.placeholder)
                .into(recipeImage);


        TextView recipeName = view.findViewById(R.id.recipeName);
        recipeName.setText(recipe.getName());

        TextView favouritesCount = view.findViewById(R.id.favouritesCount);
        favouritesCount.setText(String.valueOf(recipe.getFavourites_count()));

        TextView timeToFinish = view.findViewById(R.id.timeToFinish);
        timeToFinish.setText(String.format("%s %s", recipe.getTime_to_finish(),
                getContext().getResources().getString(R.string.minutes)));

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setRating(recipe.getRating());

        TextView recipeDescription = view.findViewById(R.id.recipeDescription);
        recipeDescription.setText(recipe.getDescription());


        if (!recipe.getTags().isEmpty()) {
            LinearLayout tags = view.findViewById(R.id.tags);

            float density = getContext().getResources().getDisplayMetrics().density;

            for (int i = 0; i < recipe.getTags().size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setBackgroundColor(Color.rgb(245, 245, 245));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textView.setTextColor(Color.BLACK);
                textView.setPadding((int) (8 * density), 0, (int) (8 * density), 0);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                if (i != 0)
                    layoutParams.setMarginStart((int) (8 * density));

                textView.setLayoutParams(layoutParams);
                tags.addView(textView);

                textView.setText(recipe.getTags().get(i));
            }
        } else {
            view.findViewById(R.id.tagsLabel).setVisibility(View.GONE);
            view.findViewById(R.id.tagsScrollView).setVisibility(View.GONE);
        }

        ImageButton favourite = view.findViewById(R.id.favourite);
        boolean isFavouritedByUser = recipe.isFavouritedByUser();

        if (isFavouritedByUser)
            favourite.setImageResource(R.drawable.favourite2);
        else
            favourite.setImageResource(R.drawable.favourite_border);

        favourite.setOnClickListener(v -> {
            String token = PreferencesManager.getToken(getContext());
            if (token.length() <= 0)
                return;
            token = "Token " + token;

            APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);

            boolean isFavourited = recipe.isFavouritedByUser();
            if (!isFavourited) {
                ((ImageButton) v).setImageResource(R.drawable.favourite2);
                recipe.setFavourites_count(recipe.getFavourites_count() + 1);
                recipe.setFavouritedByUser(true);

                APIInterface.addFavouriteRecipe(token, recipeSlug).enqueue(new emptyCallBack());

            } else {
                ((ImageButton) v).setImageResource(R.drawable.favourite_border);
                recipe.setFavourites_count(recipe.getFavourites_count() - 1);
                recipe.setFavouritedByUser(false);

                APIInterface.removeFavouriteRecipe(token, recipeSlug).enqueue(new emptyCallBack());
            }

            for (Fragment f : getActivity().getSupportFragmentManager().getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }
            }

            favouritesCount.setText(String.valueOf(recipe.getFavourites_count()));
        });
    }

    private void refreshRecipeData(View view) {
        UserModel user = recipe.getUser();
        ImageView userImage = view.findViewById(R.id.userImage);
        Glide.with(getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user)
                .into(userImage);

        TextView userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());

        Button followUser = view.findViewById(R.id.followUser);

        if (!user.getUsername().equals(PreferencesManager.getStoredUser(getContext()).getUsername())) {
            followUser.setVisibility(View.VISIBLE);

            if (recipe.getUser().isFollowedByUser())
                followUser.setText(R.string.unfollow);
            else
                followUser.setText(R.string.follow);

            followUser.setOnClickListener(v -> {
                String username = user.getUsername();

                String token = PreferencesManager.getToken(getContext());
                if (token.length() <= 0 || username.length() <= 0) {
                    return;
                }
                token = "Token " + token;

                APIInterface recipesAPIInterface = APIClient.getClient().create(APIInterface.class);

                if (!user.isFollowedByUser()) {
                    recipesAPIInterface.followUser(token, username).enqueue(new emptyCallBack());
                    ((Button) v).setText(R.string.unfollow);
                    user.setFollowedByUser(true);
                } else {
                    recipesAPIInterface.unFollowUser(token, username).enqueue(new emptyCallBack());
                    ((Button) v).setText(R.string.follow);
                    user.setFollowedByUser(false);
                }

                for (Fragment f : getActivity().getSupportFragmentManager().getFragments()) {
                    if (f instanceof OnRecipeDataChangedListener && f != this) {
                        ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                    }
                }
            });
        } else {
            followUser.setVisibility(View.INVISIBLE);
        }


        TextView timeStamp = view.findViewById(R.id.timeStamp);
        timeStamp.setText(parseTime(recipe.getTimestamp()));

        TextView favouritesCount = view.findViewById(R.id.favouritesCount);
        favouritesCount.setText(String.valueOf(recipe.getFavourites_count()));

        ImageButton favourite = view.findViewById(R.id.favourite);

        if (recipe.isFavouritedByUser())
            favourite.setImageResource(R.drawable.favourite2);
        else
            favourite.setImageResource(R.drawable.favourite_border);

        TextView recipeIngredients = view.findViewById(R.id.recipeIngredients);

        StringBuilder sb = new StringBuilder();
        List<String> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            if (i != 0) {
                sb.append("\n");
            }
            sb.append("● ");
            sb.append(ingredients.get(i));
        }

        recipeIngredients.setText(sb.toString());


        TextView recipeBody = view.findViewById(R.id.recipeBody);
        recipeBody.setText(recipe.getBody());


        if (!recipe.getImages().isEmpty()) {
            LinearLayout images = view.findViewById(R.id.images);
            float density = getContext().getResources().getDisplayMetrics().density;

            for (int i = 0; i < recipe.getImages().size(); i++) {
                ImageView imageView = new ImageView(getContext());

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                if (i != 0)
                    layoutParams.setMarginStart((int) (12 * density));

                imageView.setLayoutParams(layoutParams);
                images.addView(imageView);

                Glide.with(getContext())
                        .load(recipe.getImages().get(i))
                        .into(imageView);
            }
        } else {
            view.findViewById(R.id.imagesLabel).setVisibility(View.GONE);
            view.findViewById(R.id.imagesScrollView).setVisibility(View.GONE);
        }

        if (!recipe.getReviews().isEmpty()) {
            for (int i = 0; i < recipe.getReviews().size(); i++) {
                RecipeReviewModel review = recipe.getReviews().get(i);

                LinearLayout reviewsLayout = view.findViewById(R.id.reviews);

                View reviewView = LayoutInflater.from(getContext()).
                        inflate(R.layout.recipe_review_item, reviewsLayout, false);

                Glide.with(getContext())
                        .load(review.getUser().getImage())
                        .placeholder(R.drawable.user)
                        .into((ImageView) reviewView.findViewById(R.id.userPhoto));

                ((TextView) reviewView.findViewById(R.id.userName)).setText(review.getUser().getName());
                ((TextView) reviewView.findViewById(R.id.timeStamp)).setText(parseTime(review.getTimestamp()));
                ((TextView) reviewView.findViewById(R.id.body)).setText(review.getBody());
                ((RatingBar) reviewView.findViewById(R.id.ratingBar)).setRating(review.getRating());

                reviewsLayout.addView(reviewView);

            }
        } else {
            view.findViewById(R.id.reviewsLabel).setVisibility(View.GONE);
            view.findViewById(R.id.reviews).setVisibility(View.GONE);
        }
    }

    private String parseTime(String time) {
        Locale currentLocale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = getResources().getConfiguration().getLocales().get(0);
        } else {
            currentLocale = getResources().getConfiguration().locale;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", currentLocale);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date parsedDate = inputFormat.parse(time);
            return String.valueOf(DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        } catch (ParseException e) {
            return "";
        }
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        if (this.recipe.getSlug() != null && this.recipe.getSlug().equals(recipe.getSlug())) {
            this.recipe = recipe;
            refreshRecipeData(getView());
        }
    }

    private class emptyCallBack implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    }
}
