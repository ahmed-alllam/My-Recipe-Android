/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 26/03/20 19:48
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.myrecipe.myrecipeapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // keeps the splash activity running for 1500ms
        // then launch main activity

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, 1500);
    }
}
