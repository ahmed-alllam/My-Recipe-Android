/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 15/04/20 23:36
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.myrecipe.myrecipeapp.ui.CallBacks.OnLanguageChangedListner;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements OnLanguageChangedListner {
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang;
        if (!(lang = PreferencesManager.getStoredLanguage(this)).isEmpty()) {
            onLanguageChanged(lang, false);
        }

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
    public void onLanguageChanged(String lang, boolean refresh) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        if (refresh) {
            startActivity(new Intent(this, this.getClass()));
            finish();
        }
    }
}
