package com.progmob.android.friendkeeper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.entities.User;
import com.progmob.android.friendkeeper.utils.PasswordUtil;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    // Método de criação de usuário
    default void createUser(String name, String email, String password) {
        String salt = PasswordUtil.getSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);
        User user = new User();
        user.name = name;
        user.email = email;
        user.passwordHash = hashedPassword + ":" + salt; // Armazene o hash e o salt juntos
        insert(user);
    }

    // Método de verificação de login
    default boolean verifyUserLogin(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null) {
            String[] parts = user.passwordHash.split(":");
            String hash = parts[0];
            String salt = parts[1];
            return PasswordUtil.verifyPassword(password, salt, hash);
        }
        return false;
    }

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE id = :id")
    User getUserById(int id);
}