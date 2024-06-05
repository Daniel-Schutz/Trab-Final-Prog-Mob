package com.progmob.android.friendkeeper.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
    private static final String PREFS_NAME = "FriendKeeperPrefs";
    private static final String KEY_NIGHT_MODE = "night_mode";

    public static void setNightMode(Context context, boolean isNightMode) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, isNightMode);
        editor.apply();
    }

    public static boolean isNightMode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_NIGHT_MODE, false);
    }
}
