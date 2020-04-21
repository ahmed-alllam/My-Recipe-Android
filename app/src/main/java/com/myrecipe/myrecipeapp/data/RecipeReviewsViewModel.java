/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 21/04/20 21:25
 */

package com.myrecipe.myrecipeapp.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeReviewModel;
import com.myrecipe.myrecipeapp.models.ReviewsResultModel;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeReviewsViewModel extends ViewModel {
    public MutableLiveData<ReviewsResultModel> reviews = new MutableLiveData<>();
    public MutableLiveData<RecipeReviewModel> review = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();


    public void getReviews(Context context, String slug, int limit, int offset) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        String token = PreferencesManager.getToken(context);

        Call<ReviewsResultModel> call;

        if (token.length() > 0) {
            token = "Token " + token;
            call = RecipesAPIInterface.getRecipeReviews(token, slug, limit, offset);
        } else {
            call = RecipesAPIInterface.getRecipeReviews(slug, limit, offset);
        }

        call.enqueue(new Callback<ReviewsResultModel>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResultModel> call, @NonNull Response<ReviewsResultModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.network_error);
                else if (response.body().getReviews().isEmpty())
                    error.setValue(R.string.list_empty);
                else
                    reviews.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResultModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }

    public void addReview(Context context, String slug, RecipeReviewModel reviewModel) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        String token = PreferencesManager.getToken(context);
        token = "Token " + token;

        RecipesAPIInterface.addRecipeReview(token, slug, reviewModel).enqueue(new Callback<RecipeReviewModel>() {
            @Override
            public void onResponse(@NonNull Call<RecipeReviewModel> call, @NonNull Response<RecipeReviewModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.network_error);
                else
                    review.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RecipeReviewModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }

    public void editReview(Context context, String recipeSlug, String slug, RecipeReviewModel reviewModel) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        String token = PreferencesManager.getToken(context);
        token = "Token " + token;

        RecipesAPIInterface.editRecipeReview(token, recipeSlug, slug, reviewModel).enqueue(new Callback<RecipeReviewModel>() {
            @Override
            public void onResponse(@NonNull Call<RecipeReviewModel> call, @NonNull Response<RecipeReviewModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.network_error);
                else
                    review.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RecipeReviewModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }

    public void deleteReview(Context context, String recipeSlug, String slug) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        String token = PreferencesManager.getToken(context);
        token = "Token " + token;

        RecipesAPIInterface.deleteRecipeReview(token, recipeSlug, slug).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }
}
