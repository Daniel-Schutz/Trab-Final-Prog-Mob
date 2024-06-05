package com.progmob.android.friendkeeper.utils;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    public static void setNightMode(boolean isNightMode) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
