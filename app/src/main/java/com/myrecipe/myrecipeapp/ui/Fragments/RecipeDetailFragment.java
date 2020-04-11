/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 11/04/20 23:44
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecipesAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeDetailFragment extends Fragment {

    private RecipeModel recipe;
    private BaseRecipesAdapter adapter;

    RecipeDetailFragment(RecipeModel recipe) {
        this.recipe = recipe;
    }

    RecipeDetailFragment(RecipeModel recipe, BaseRecipesAdapter adapter) {
        this.recipe = recipe;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (adapter != null) {
            adapter.updateRecipe(recipe);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecipeDetailViewModel viewModel = new ViewModelProvider(this)
                .get(RecipeDetailViewModel.class);

        viewModel.recipe.observe(getViewLifecycleOwner(), (recipe) -> refreshRecipeData(recipe, view));

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            // todo
        });

        String recipeSlug = recipe.getSlug();

        viewModel.getRecipe(recipeSlug);


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

        ImageButton favourite = view.findViewById(R.id.favourite);
        boolean isFavouritedByUser = recipe.isFavouritedByUser();

        if (isFavouritedByUser)
            favourite.setImageResource(R.drawable.favourite2);

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

            favouritesCount.setText(String.valueOf(recipe.getFavourites_count()));
        });
    }

    private void refreshRecipeData(RecipeModel recipe, View view) {
        UserModel user = recipe.getUser();
        ImageView userImage = view.findViewById(R.id.userImage);
        Glide.with(getContext())
                .load(user.getImage())
                .placeholder(R.drawable.user)
                .into(userImage);

        TextView userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());
        // todo follow button

        TextView timeStamp = view.findViewById(R.id.timeStamp);
        timeStamp.setText(parseTime(recipe.getTimestamp()));

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

    private class emptyCallBack implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    }
}
