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

import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.LoginViewModel;
import com.myrecipe.myrecipeapp.data.SignupViewModel;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

public class SignupActivity extends BaseActivity {
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        signupViewModel.user.observe(this, user -> {
            System.out.println(email + "  hiii   " + password);
            loginViewModel.loginUser(email, password);
        });
        signupViewModel.error.observe(this, error -> {
            ((TextView) findViewById(R.id.error)).setText(error);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        });

        loginViewModel.userLoginModel.observe(this, userTokenModel -> {
            PreferencesManager.setToken(this, userTokenModel.getToken());
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.putExtra("token", userTokenModel.getToken());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        loginViewModel.error.observe(this, error -> {
            ((TextView) findViewById(R.id.error)).setText(error);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    public void signup(View view) {
        String name = ((EditText) findViewById(R.id.nameField)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.confirmPasswordField)).getText().toString();

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        TextView error = findViewById(R.id.error);


        if (name.isEmpty()) {
            error.setText(R.string.wrong_name);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        } else if (!validEmail(email)) {
            error.setText(R.string.wrong_email);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        } else if (!validPassword(password, password2)) {
            error.setText(R.string.wrong_password);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        } else {
            this.email = email;
            this.password = password;
            signupViewModel.signup(name, email, password);
        }
    }

    private boolean validEmail(String email) {
        return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean validPassword(String password, String password2) {
        return password.equals(password2) && password.length() >= 5;
    }
}
