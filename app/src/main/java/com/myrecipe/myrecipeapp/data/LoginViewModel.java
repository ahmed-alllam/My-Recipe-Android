/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 03/04/20 21:32
 */

package com.myrecipe.myrecipeapp.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.models.UserLoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<UserLoginModel> userLoginModel = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void loginUser(String email, String password) {
        APIInterface loginAPIInterface = APIClient.getClient().create(APIInterface.class);

        loginAPIInterface.loginUser(email, password).enqueue(new Callback<UserLoginModel>() {
            @Override
            public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                userLoginModel.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserLoginModel> call, Throwable t) {
                // todo
            }
        });
    }
}
