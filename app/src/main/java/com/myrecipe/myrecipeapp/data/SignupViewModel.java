/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 15:41
 */

package com.myrecipe.myrecipeapp.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends ViewModel {
    public MutableLiveData<UserModel> user = new MutableLiveData<>();
    public MutableLiveData<Integer> error = new MutableLiveData<>();

    public void signup(String name, String email, String password) {
        APIInterface signupAPIInterface = APIClient.getClient().create(APIInterface.class);

        signupAPIInterface.signup(name, email, password).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (!response.isSuccessful())
                    error.setValue(R.string.duplicate_email);
                else
                    user.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                error.setValue(R.string.network_error);
            }
        });
    }
}
