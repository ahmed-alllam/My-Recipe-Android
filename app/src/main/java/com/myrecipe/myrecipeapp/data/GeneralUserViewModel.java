/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 18:12
 */

package com.myrecipe.myrecipeapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralUserViewModel extends ViewModel {
    public MutableLiveData<UserModel> user = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getUserProfile(String username) {
        APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);

        APIInterface.getUserProfile(username).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.network_error);
                else
                    user.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
