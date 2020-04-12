/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 12/04/20 22:50
 */

package com.myrecipe.myrecipeapp.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.models.RecipeModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailViewModel extends ViewModel {
    public MutableLiveData<RecipeModel> recipe = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getRecipe(Context context, String slug) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<RecipeModel> call;

        String token = PreferencesManager.getToken(context);

        if (token.length() > 0) {
            token = "Token " + token;
            call = apiInterface.getRecipe(token, slug);
        } else {
            call = apiInterface.getRecipe(slug);
        }

        call.enqueue(new Callback<RecipeModel>() {
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
