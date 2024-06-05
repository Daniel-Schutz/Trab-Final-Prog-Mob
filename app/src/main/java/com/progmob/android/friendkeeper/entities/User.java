package com.progmob.android.friendkeeper.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String email;
    public String passwordHash;  // hashed password
    public String profilePictureUri; // URI to the profile picture
}
