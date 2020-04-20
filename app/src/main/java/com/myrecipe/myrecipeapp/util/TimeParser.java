/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 16:53
 */

package com.myrecipe.myrecipeapp.util;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeParser {
    // helper class used to parse time relatively

    public static String parseTime(Context context, String time) {
        Locale currentLocale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            currentLocale = context.getResources().getConfiguration().locale;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", currentLocale);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date parsedDate = inputFormat.parse(time);
            return String.valueOf(DateUtils.getRelativeTimeSpanString(parsedDate.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        } catch (ParseException e) {
            return "";
        }
    }
}
