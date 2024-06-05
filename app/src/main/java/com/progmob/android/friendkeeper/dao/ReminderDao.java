package com.progmob.android.friendkeeper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.entities.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {
    @Insert
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Query("SELECT * FROM Reminder WHERE contactId = :contactId")
    List<Reminder> getRemindersByContactId(int contactId);

    @Query("SELECT * FROM Reminder WHERE id = :id")
    Reminder getReminderById(int id);
}