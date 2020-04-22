/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 17:56
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.graphics.Color;
import android.os.Bundle;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.CallBacks.OnUserProfileChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.RecipeDetailViewModel;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.util.PreferencesManager;
import com.myrecipe.myrecipeapp.util.TimeParser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeDetailFragment extends Fragment implements OnRecipeDataChangedListener, OnUserProfileChangedListener {

    private RecipeModel recipe;
    private boolean loading = true;

    public RecipeDetailFragment(RecipeModel recipe) {
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
            loading = false;
            loadNewRecipeData(view);
            refreshRecipeData(view, null);
        });


        String recipeSlug = recipe.getSlug();

        viewModel.getRecipe(getContext(), recipeSlug);


        view.findViewById(R.id.backButton).setOnClickListener((v) -> getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_exit, R.anim.fragment_exit)
                .remove(this)
                .commit());

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
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
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

            for (Fragment f : ((MainActivity) getActivity()).getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }
            }

            favouritesCount.setText(String.valueOf(recipe.getFavourites_count()));
        });

        Button followUser = view.findViewById(R.id.followUser);

        followUser.setOnClickListener(v -> {
            UserModel me = PreferencesManager.getStoredUser(getContext());
            UserModel user = recipe.getUser();

            if (user == null) return;

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
                user.setFollowers_count(user.getFollowers_count() + 1);
                me.setFollowings_count(me.getFollowings_count() + 1);
            } else {
                recipesAPIInterface.unFollowUser(token, username).enqueue(new emptyCallBack());
                ((Button) v).setText(R.string.follow);
                user.setFollowedByUser(false);
                user.setFollowers_count(user.getFollowers_count() - 1);
                me.setFollowings_count(me.getFollowings_count() - 1);
            }

            for (Fragment f : ((MainActivity) getActivity()).getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }

                if (f instanceof OnUserProfileChangedListener && f != this) {
                    ((OnUserProfileChangedListener) f).onUserProfileChanged(me, true);
                    ((OnUserProfileChangedListener) f).onUserProfileChanged(user, false);
                }
            }
        });

        view.findViewById(R.id.recipesUserContainer).setOnClickListener(v -> launchFragment(new GeneralUsersProfileFragment(recipe.getUser())));

        view.findViewById(R.id.allreviewsLabel).setOnClickListener(v -> launchFragment(new RecipeReviewsFragment(recipe)));

        ((RatingBar) view.findViewById(R.id.ratingBar2)).setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                DialogFragment dialog = new AddReviewFragment(recipe, rating, null);
                ((MainActivity) getActivity()).addFragment(dialog);
                dialog.show(getChildFragmentManager(), "AddReviewDialog");
            }
        });
    }

    private void loadNewRecipeData(View view) {
        UserModel user = recipe.getUser();
        ImageView userImage = view.findViewById(R.id.userImage);
        Glide.with(getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user_icon)
                .into(userImage);

        TextView userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView timeStamp = view.findViewById(R.id.timeStamp);
        timeStamp.setText(TimeParser.parseTime(getContext(), recipe.getTimestamp()));

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
                CardView cardView = new CardView(getContext());

                LinearLayout.LayoutParams imagelayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                LinearLayout.LayoutParams cardlayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                if (i != 0)
                    cardlayoutParams.setMarginStart((int) (8 * density));

                cardView.setLayoutParams(cardlayoutParams);
                imageView.setLayoutParams(imagelayoutParams);
                cardView.setRadius(25 * density);

                cardView.addView(imageView);
                images.addView(cardView);

                Glide.with(getContext())
                        .load(recipe.getImages().get(i))
                        .into(imageView);
            }
        } else {
            view.findViewById(R.id.imagesLabel).setVisibility(View.GONE);
            view.findViewById(R.id.imagesScrollView).setVisibility(View.GONE);
        }
    }

    private void refreshRecipeData(View view, UserModel me) {
        UserModel user = recipe.getUser();

        Button followUser = view.findViewById(R.id.followUser);

        if (me == null)
            me = PreferencesManager.getStoredUser(getContext());

        if (!me.getUsername().equals("") && !user.getUsername().equals(me.getUsername())) {
            followUser.setVisibility(View.VISIBLE);

            if (user.isFollowedByUser())
                followUser.setText(R.string.unfollow);
            else
                followUser.setText(R.string.follow);
        } else {
            followUser.setVisibility(View.INVISIBLE);
        }

        TextView favouritesCount = view.findViewById(R.id.favouritesCount);
        favouritesCount.setText(String.valueOf(recipe.getFavourites_count()));

        ImageButton favourite = view.findViewById(R.id.favourite);

        if (recipe.isFavouritedByUser())
            favourite.setImageResource(R.drawable.favourite2);
        else
            favourite.setImageResource(R.drawable.favourite_border);


        if (PreferencesManager.getToken(getContext()).length() > 0) {
            RatingBar ratingBar = view.findViewById(R.id.ratingBar2);
            ratingBar.setVisibility(View.VISIBLE);

            ratingBar.setRating(recipe.getUsersRating());
            if (recipe.getUsersRating() != 0)
                ratingBar.setIsIndicator(true);
            else
                ratingBar.setIsIndicator(false);

            view.findViewById(R.id.addReviewLabel).setVisibility(View.VISIBLE);
        }

        if (!recipe.getReviews().isEmpty()) {
            if (recipe.getReviews().size() > 3) {
                view.findViewById(R.id.allreviewsLabel).setVisibility(View.VISIBLE);
            }

            TextView reviewsCount = view.findViewById(R.id.reviewsCount);
            reviewsCount.setText(String.format("(%s)", recipe.getReviews_count()));

            LinearLayout reviewsLayout = view.findViewById(R.id.reviews);
            reviewsLayout.removeAllViews();

            for (int i = 0; i < recipe.getReviews().size(); i++) {
                RecipeReviewModel review = recipe.getReviews().get(i);

                View reviewView = LayoutInflater.from(getContext()).
                        inflate(R.layout.recipe_review_item, reviewsLayout, false);

                Glide.with(getContext())
                        .load(review.getUser().getImage())
                        .placeholder(R.drawable.user_icon)
                        .into((ImageView) reviewView.findViewById(R.id.userPhoto));

                ((TextView) reviewView.findViewById(R.id.userName)).setText(review.getUser().getName());
                ((TextView) reviewView.findViewById(R.id.timeStamp)).setText(TimeParser.parseTime(getContext(), review.getTimestamp()));
                ((TextView) reviewView.findViewById(R.id.body)).setText(review.getBody());
                ((RatingBar) reviewView.findViewById(R.id.ratingBar)).setRating(review.getRating());

                reviewsLayout.addView(reviewView);

                if (i == 2) {
                    break;
                }
            }
        } else {
            view.findViewById(R.id.reviewsLabel).setVisibility(View.GONE);
            view.findViewById(R.id.reviews).setVisibility(View.GONE);
        }
    }

    private void launchFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(getView().getId(), fragment)
                .commit();
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }

    @Override
    public void onRecipeChanged(RecipeModel newRecipe) {
        if (recipe.getSlug() != null && recipe.getSlug().equals(newRecipe.getSlug()) && !loading) {
            recipe = recipe.checkNullFields(newRecipe);
            refreshRecipeData(getView(), null);
        }
    }

    @Override
    public void onUserProfileChanged(UserModel user, boolean isCurrentUser) {
        //todo: test it
        if (!loading && !isCurrentUser && recipe.getUser() != null &&
                recipe.getUser().getUsername().equals(user.getUsername())) {
            recipe.setUser(user);
            refreshRecipeData(getView(), null);
        }

        if (!loading && isCurrentUser)
            refreshRecipeData(getView(), user);
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
