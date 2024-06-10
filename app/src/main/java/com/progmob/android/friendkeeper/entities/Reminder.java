package com.progmob.android.friendkeeper.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Reminder",
        foreignKeys = @ForeignKey(entity = Contact.class,
                parentColumns = "id",
                childColumns = "contactId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "contactId")})
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int contactId;  // Foreign key to Contact
    public String type;  // 'Birthday' or 'Event'
    public String date;
    public String notificationMessage;
}