/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 04/04/20 01:22
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.LoginViewModel;
import com.myrecipe.myrecipeapp.data.PreferencesManager;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.userLoginModel.observe(this, userLoginModel -> {
            PreferencesManager.setToken(this, userLoginModel.getToken());
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            finish();
        });
        loginViewModel.error.observe(this, error -> {
            ((TextView) findViewById(R.id.error)).setText(error);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        });
    }

    public void login(View view) {
        String email = ((EditText) findViewById(R.id.emailField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        if (!validEmail(email) || !validPassword(password)) {
            ((TextView) findViewById(R.id.error)).setText(R.string.wrong_email_or_password);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        } else {
            loginViewModel.loginUser(email, password);
        }
    }

    private boolean validEmail(String email) {
        return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean validPassword(String password) {
        return password.length() >= 5;
    }
}
