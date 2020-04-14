/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 14/04/20 23:38
 */

package com.myrecipe.myrecipeapp.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipesResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRecipesModelView extends ViewModel {
    public MutableLiveData<RecipesResultModel> recipes = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getUsersRecipes(Context context, String username, int limit, int offset) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        if (username.isEmpty()) {
            error.setValue(R.string.feed_empty);
            return;
        }

        String token = PreferencesManager.getToken(context);

        Call<RecipesResultModel> call;

        if (token.length() > 0) {
            token = "Token " + token;
            call = RecipesAPIInterface.getUsersRecipes(token, username, limit, offset);
        } else {
            call = RecipesAPIInterface.getUsersRecipes(username, limit, offset);
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
