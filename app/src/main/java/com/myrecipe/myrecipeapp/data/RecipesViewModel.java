/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 01/04/20 16:38
 */

package com.myrecipe.myrecipeapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipesResultModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesViewModel extends ViewModel {
    public MutableLiveData<List<RecipeModel>> recipes = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getFeed(int limit, int offset) {
        // todo use rxjava
        new RecipesClient().getFeed(limit, offset).enqueue(new Callback<RecipesResultModel>() {
            @Override
            public void onResponse(@NonNull Call<RecipesResultModel> call, @NonNull Response<RecipesResultModel> response) {
                // if (response.body().getRecipes().isEmpty() && offset > 0) {
                    // TODO:
                // return;
                // }
                if (response.body().getRecipes().isEmpty() && offset == 0) {
                    error.setValue(R.string.feed_empty);
                } else {
                    recipes.setValue(response.body().getRecipes());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipesResultModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
