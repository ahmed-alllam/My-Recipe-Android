/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 16:53
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.LoginViewModel;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

public class LoginActivity extends BaseActivity {
    private static final int LAUNCH_SIGNUP_ACTIVITY = 0;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.userLoginModel.observe(this, userLoginModel -> {
            PreferencesManager.setToken(this, userLoginModel.getToken());
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.putExtra("token", userLoginModel.getToken());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        loginViewModel.error.observe(this, error -> {
            ((TextView) findViewById(R.id.error)).setText(error);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        findViewById(R.id.signUpLabel).setOnClickListener(v -> {
            startActivityForResult(new Intent(this, SignupActivity.class), LAUNCH_SIGNUP_ACTIVITY);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SIGNUP_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    setResult(RESULT_OK, data);
                }
            }
            finish();
        }
    }
}
