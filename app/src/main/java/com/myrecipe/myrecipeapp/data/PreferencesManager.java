/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 04/04/20 20:30
 */

package com.myrecipe.myrecipeapp.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    //a helper class used to make some changes or get some data from the shared preferences

    public static String TOKEN_PREFERENCE = "user_token";
    // todo add token as a variable in memory

    private static String getPreference(Context context, String preference, String default_value) {
        //returns the value of a shared preference given the key
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        return prefs.getString(preference, default_value);
    }

    private static void setPreference(Context context, String preference, String value) {
        // makes some changes to the shared preferences
        // or adds new preference
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(preference, value);
        editor.apply();
    }

    public static void setToken(Context context, String token) {
        // sets a user token value in shared preferences
        setPreference(context, TOKEN_PREFERENCE, token);
    }

    public static String getToken(Context context) {
        // gets the stored user token in shared preferences
        return getPreference(context, TOKEN_PREFERENCE, "");
    }
}

