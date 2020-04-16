/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 23:47
 */

package com.myrecipe.myrecipeapp.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UsersResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedUsersViewModel extends ViewModel {
    public MutableLiveData<UsersResultModel> users = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getUsersFollowers(Context context, String username, int limit, int offset) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        if (username.isEmpty()) {
            error.setValue(R.string.list_empty);
            return;
        }

        String token = PreferencesManager.getToken(context);

        Call<UsersResultModel> call;

        if (token.length() > 0) {
            token = "Token " + token;
            call = RecipesAPIInterface.getUsersFollowers(token, username, limit, offset);
        } else {
            call = RecipesAPIInterface.getUsersFollowers(username, limit, offset);
        }

        enqueueCall(call);
    }

    public void getUsersFollowings(Context context, String username, int limit, int offset) {
        APIInterface RecipesAPIInterface = APIClient.getClient().create(APIInterface.class);

        if (username.isEmpty()) {
            error.setValue(R.string.list_empty);
            return;
        }

        String token = PreferencesManager.getToken(context);

        Call<UsersResultModel> call;

        if (token.length() > 0) {
            token = "Token " + token;
            call = RecipesAPIInterface.getUsersFollowings(token, username, limit, offset);
        } else {
            call = RecipesAPIInterface.getUsersFollowings(username, limit, offset);
        }

        enqueueCall(call);
    }

    private void enqueueCall(Call<UsersResultModel> call) {
        call.enqueue(new Callback<UsersResultModel>() {
            @Override
            public void onResponse(@NonNull Call<UsersResultModel> call, @NonNull Response<UsersResultModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.network_error);
                else if (response.body().getUsers().isEmpty())
                    error.setValue(R.string.list_empty);
                else
                    users.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UsersResultModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
