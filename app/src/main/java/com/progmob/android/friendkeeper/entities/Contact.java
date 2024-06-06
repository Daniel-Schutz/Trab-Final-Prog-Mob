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
    public int userId;  // Chave estrangeira para usuário
    public String name;
    public String phoneNumber;
    public String email;
    public String address;
    public String birthdate;
    public String photoUri; // URI para a foto do contato
    public String location; // Informações de localização (pode ser latitude, longitude ou nome de um lugar)
}