/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 03/04/20 21:32
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        String email = ((EditText) findViewById(R.id.emailField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        // todo add validation

        LoginViewModel loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        loginViewModel.loginUser(email, password);

        loginViewModel.userLoginModel.observe(this, userLoginModel -> {
            // todo
        });
        loginViewModel.error.observe(this, error -> {
            // todo
        });
    }
}
