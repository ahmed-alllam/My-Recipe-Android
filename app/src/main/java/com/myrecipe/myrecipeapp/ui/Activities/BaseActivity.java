/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 16:53
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myrecipe.myrecipeapp.util.PreferencesManager;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang;
        if (!(lang = PreferencesManager.getStoredLanguage(this)).isEmpty()) {
            changeLanguage(lang, false);
        }
    }

    public void changeLanguage(String lang, boolean refresh) {
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
