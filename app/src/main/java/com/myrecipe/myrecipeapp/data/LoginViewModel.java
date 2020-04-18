/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 15:41
 */

package com.myrecipe.myrecipeapp.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UserTokenModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<UserTokenModel> userLoginModel = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void loginUser(String email, String password) {
        APIInterface loginAPIInterface = APIClient.getClient().create(APIInterface.class);

        loginAPIInterface.loginUser(email, password).enqueue(new Callback<UserTokenModel>() {
            @Override
            public void onResponse(Call<UserTokenModel> call, Response<UserTokenModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.wrong_email_or_password);
                else
                    userLoginModel.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserTokenModel> call, Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
