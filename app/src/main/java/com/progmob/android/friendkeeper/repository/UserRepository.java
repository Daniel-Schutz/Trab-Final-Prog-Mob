package com.progmob.android.friendkeeper.repository;

import com.progmob.android.friendkeeper.dao.UserDao;
import com.progmob.android.friendkeeper.entities.User;

/**
 * A classe UserRepository é responsável por gerenciar as operações de banco de dados para a entidade User.
 * Ela abstrai a interação com o banco de dados e fornece métodos para editar e recuperar perfis de usuários.
 */
public class UserRepository {
    // Data Access Object (DAO) para a entidade User
    private final UserDao userDao;

    /**
     * Construtor da classe UserRepository.
     * Inicializa o DAO de usuários.
     *
     * @param userDao O DAO de usuários.
     */
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Edita o perfil de um usuário existente no banco de dados.
     *
     * @param userId O ID do usuário a ser editado.
     * @param name O novo nome do usuário.
     * @param email O novo email do usuário.
     */
    public void editUserProfile(int userId, String name, String email) {
        User user = userDao.getUserById(userId);
        if (user != null) {
            user.name = name;
            user.email = email;
            userDao.update(user);
        }
    }

    /**
     * Retorna o usuário com o ID especificado.
     *
     * @param userId O ID do usuário a ser retornado.
     * @return O usuário com o ID especificado.
     */
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }
}
