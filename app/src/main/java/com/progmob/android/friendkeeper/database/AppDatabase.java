package com.progmob.android.friendkeeper.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.dao.ReminderDao;
import com.progmob.android.friendkeeper.dao.UserDao;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.entities.Reminder;
import com.progmob.android.friendkeeper.entities.User;

@Database(entities = {User.class, Contact.class, Reminder.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ContactDao contactDao();
    public abstract ReminderDao reminderDao();
}