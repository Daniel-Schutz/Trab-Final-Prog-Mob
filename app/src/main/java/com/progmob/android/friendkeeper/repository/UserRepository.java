package com.progmob.android.friendkeeper.repository;

import com.progmob.android.friendkeeper.dao.UserDao;
import com.progmob.android.friendkeeper.entities.User;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void editUserProfile(int userId, String name, String email, String profilePictureUri) {
        User user = userDao.getUserById(userId);
        if (user != null) {
            user.name = name;
            user.email = email;
            user.profilePictureUri = profilePictureUri;
            userDao.update(user);
        }
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }
}
