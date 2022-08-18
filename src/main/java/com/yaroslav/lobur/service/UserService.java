package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Role;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long registerUser(User user) {
        try {
            userDao.checkUniqueFields(user);
            return userDao.insertUser(user);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public long signIn(String email, String password) {
        try {
            User user = userDao.findUserByEmail(email);
            if (PasswordEncryptor.getSHA1String(password).equals(user.getPassword())) {
                return user.getId();
            }
            throw new DBExceptionMessages(List.of("signin.error.wrong_password"));
        } catch (EntityNotFoundException e) {
            logger.info("User not found");
            throw new DBExceptionMessages(List.of("signin.error.email_not_found"));
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public User getUserById(long id) {
        try {
            return userDao.findUserById(id);
        } catch (EntityNotFoundException e) {
            throw new DBExceptionMessages(List.of("user.not_found"));
        }
    }

    public List<User> getUsersByRole(Role role) {
        return userDao.findAllByRole(role);
    }
}
