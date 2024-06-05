package com.progmob.android.friendkeeper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM Contact WHERE userId = :userId")
    List<Contact> getContactsByUserId(int userId);

    @Query("SELECT * FROM Contact WHERE id = :id")
    Contact getContactById(int id);
}