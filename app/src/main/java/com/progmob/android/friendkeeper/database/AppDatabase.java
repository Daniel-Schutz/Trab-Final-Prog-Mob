package com.progmob.android.friendkeeper.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.dao.ReminderDao;
import com.progmob.android.friendkeeper.dao.UserDao;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.entities.Reminder;
import com.progmob.android.friendkeeper.entities.User;

@Database(entities = {User.class, Contact.class, Reminder.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public abstract UserDao userDao();
    public abstract ReminderDao reminderDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "friendkeeper_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
