package com.progmob.android.friendkeeper.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE))
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;  // Foreign key to User
    public String name;
    public String phoneNumber;
    public String email;
    public String address;
    public String birthdate;
    public String photoUri; // URI to the contact's photo
    public String location; // Location information (could be latitude,longitude or a place name)
}