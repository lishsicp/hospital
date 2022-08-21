package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Role;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final UserDao userDao;
    private static final DaoFactory daoFactory;

    static {
        daoFactory = DaoFactory.getDaoFactory();
        userDao = daoFactory.getUserDao();
    }
    public long registerUser(User user) {
        try {
            Connection con = daoFactory.open();
            userDao.checkUniqueFields(con, user);
            return userDao.insertUser(con, user);
        } catch (UnknownSqlException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public long signIn(String email, String password) {
        try {
            Connection con = daoFactory.open();
            User user = userDao.findUserByEmail(con, email);
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
            Connection con = daoFactory.open();
            return userDao.findUserById(con, id);
        } catch (EntityNotFoundException e) {
            throw new DBExceptionMessages(List.of("user.not_found"));
        }
    }

    public List<User> getUsersByRole(Role role) {
        return userDao.findAllByRole(daoFactory.open(), role);
    }
}
