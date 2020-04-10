/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 10/04/20 20:43
 */

package com.myrecipe.myrecipeapp.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.models.RecipeModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailViewModel extends ViewModel {
    public MutableLiveData<RecipeModel> recipe = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getRecipe(String slug) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        apiInterface.getRecipe(slug).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    recipe.setValue(response.body());
                } else {
                    // todo
                }

            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                error.setValue(1);  // todo
            }
        });
    }
}
