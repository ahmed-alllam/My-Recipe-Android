/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 05/04/20 18:52
 */

package com.myrecipe.myrecipeapp.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    public MutableLiveData<UserModel> userProfile = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void getMyProfile(String token) {
        APIInterface userAPIInterface = APIClient.getClient().create(APIInterface.class);

        userAPIInterface.getMyProfile("Token " + token).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() >= 400)
                    error.setValue(R.string.profile_network_error);
                else
                    userProfile.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                error.setValue(R.string.profile_network_error);
            }
        });
    }
}
