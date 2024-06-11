package com.progmob.android.friendkeeper.entities;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Contact",
        foreignKeys = @ForeignKey(entity = Address.class,
                parentColumns = "id",
                childColumns = "addressId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "addressId")})
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String name;
    public String phoneNumber;
    public String email;
    public int addressId;
    public String birthdate;
    public String photoUri;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUri() {
        return photoUri != null ? Uri.parse(photoUri) : null;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri != null ? photoUri.toString() : null;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
