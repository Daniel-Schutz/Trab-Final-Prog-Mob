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

/**
 * Interface UserDao é um DAO (Data Access Object) para a entidade User.
 * Fornece métodos para realizar operações CRUD (Create, Read, Update, Delete) no banco de dados.
 */
@Dao
public interface UserDao {

    /**
     * Insere um novo usuário no banco de dados.
     *
     * @param user A entidade User a ser inserida.
     */
    @Insert
    void insert(User user);

    /**
     * Busca um usuário pelo seu e-mail.
     *
     * @param email O e-mail do usuário.
     * @return A entidade User correspondente ao e-mail fornecido.
     */
    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    /**
     * Cria um novo usuário no banco de dados.
     *
     * @param name O nome do usuário.
     * @param email O e-mail do usuário.
     * @param password A senha do usuário.
     * @return true se o usuário foi criado com sucesso, false caso contrário.
     */
    default boolean createUser(String name, String email, String password) {
        try {
            // Gera um salt e um hash para a senha
            String salt = PasswordUtil.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            // Cria um novo usuário
            User user = new User();
            user.name = name;
            user.email = email;
            user.passwordHash = hashedPassword + ":" + salt; // Armazena o hash e o salt juntos
            // Insere o usuário no banco de dados
            insert(user);
            return true; // Usuário adicionado com sucesso
        } catch (Exception e) {
            Log.e("UserDao", String.valueOf(R.string.erro_registro_usuario), e);
            return false; // Falha na inserção do usuário
        }
    }

    /**
     * Verifica as credenciais de login de um usuário.
     *
     * @param email O e-mail do usuário.
     * @param password A senha do usuário.
     * @return true se as credenciais forem válidas, false caso contrário.
     */
    default boolean verifyUserLogin(String email, String password) {
        // Busca o usuário pelo e-mail
        User user = getUserByEmail(email);
        if (user != null) {
            // Divide o hash e o salt armazenados
            String[] parts = user.passwordHash.split(":");
            String hash = parts[0];
            String salt = parts[1];
            // Verifica a senha
            return PasswordUtil.verifyPassword(password, salt, hash);
        }
        return false;
    }

    /**
     * Atualiza um usuário existente no banco de dados.
     *
     * @param user A entidade User com as novas informações.
     */
    @Update
    void update(User user);

    /**
     * Deleta um usuário do banco de dados.
     *
     * @param user A entidade User a ser deletada.
     */
    @Delete
    void delete(User user);

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O ID do usuário.
     * @return A entidade User correspondente ao ID fornecido.
     */
    @Query("SELECT * FROM User WHERE id = :id")
    User getUserById(int id);
}
