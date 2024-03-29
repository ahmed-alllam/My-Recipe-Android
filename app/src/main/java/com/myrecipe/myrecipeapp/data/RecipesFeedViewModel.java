/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 16:53
 */

package com.myrecipe.myrecipeapp.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipesResultModel;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesFeedViewModel extends ViewModel {
    public MutableLiveData<RecipesResultModel> recipes = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getFeed(Context context, int limit, int offset) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        String token = PreferencesManager.getToken(context);

        Call<RecipesResultModel> call;

        if (token.length() > 0) {
            token = "Token " + token;
            call = RecipesAPIInterface.getFeed(token, limit, offset);
        } else {
            call = RecipesAPIInterface.getFeed(limit, offset);
        }

        call.enqueue(new Callback<RecipesResultModel>() {
            @Override
            public void onResponse(@NonNull Call<RecipesResultModel> call, @NonNull Response<RecipesResultModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.network_error);
                else if (response.body().getRecipes().isEmpty())
                    error.setValue(R.string.feed_empty);
                else
                    recipes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RecipesResultModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
