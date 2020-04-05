/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 05/04/20 18:52
 */

package com.myrecipe.myrecipeapp.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipesResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesModelView extends ViewModel {
    public MutableLiveData<RecipesResultModel> favouriteRecipes = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getFavouriteRecipes(Context context, String username, int limit, int offset) {

        String token = PreferencesManager.getToken(context);
        if (token.length() <= 0) {
            error.setValue(R.string.favourites_empty);
            return;
        }

        token = "Token " + token;

        APIInterface recipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        recipesAPIInterface.getFavouriteRecipes(token, username, limit, offset).enqueue(new Callback<RecipesResultModel>() {
            @Override
            public void onResponse(Call<RecipesResultModel> call, Response<RecipesResultModel> response) {
                if (response.code() >= 400)
                    error.setValue(R.string.network_error);
                else if (response.body().getRecipes().isEmpty())
                    error.setValue(R.string.favourites_empty);
                else
                    favouriteRecipes.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RecipesResultModel> call, Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
