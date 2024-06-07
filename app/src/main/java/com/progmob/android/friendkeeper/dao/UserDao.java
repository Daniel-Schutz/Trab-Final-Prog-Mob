package com.progmob.android.friendkeeper.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.entities.User;
import com.progmob.android.friendkeeper.utils.PasswordUtil;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    // Método de criação de usuário
    default boolean createUser(String name, String email, String password) {
        try {
            String salt = PasswordUtil.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            User user = new User();
            user.name = name;
            user.email = email;
            user.passwordHash = hashedPassword + ":" + salt; // Armazene o hash e o salt juntos
            insert(user);
            return true; // Usuário adicionado com sucesso
        } catch (Exception e) {
            Log.e("UserDao", String.valueOf(R.string.erro_registro_usuario), e);
            return false; // Falha na inserção do usuário
        }
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