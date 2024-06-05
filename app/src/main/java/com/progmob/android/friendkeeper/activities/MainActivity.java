package com.progmob.android.friendkeeper.activities;

import android.app.Application;
import androidx.room.Room;

import com.progmob.android.friendkeeper.database.AppDatabase;

public class MainActivity extends Application {
    private static MainActivity instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "friendkeeper-db").build();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}