/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 23:54
 */

package com.myrecipe.myrecipeapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.models.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesViewModel extends ViewModel {
    public MutableLiveData<List<RecipeModel>> recipes = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    public void getFeed(int limit, int offset) {
        // todo use rxjava
        new RecipesClient().getFeed(limit, offset).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeModel>> call, @NonNull Response<List<RecipeModel>> response) {
                if (response.body().isEmpty()) {
                    // todo: add to error
                    return;
                }
                recipes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeModel>> call, @NonNull Throwable t) {
                error.setValue("No Internet");
            }
        });
    }
}
