package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
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

    private final DaoFactory daoFactory;
    private final UserDao userDao;


    public UserService(DaoFactory daoFactory, UserDao userDao) {
        this.daoFactory = daoFactory;
        this.userDao = userDao;
    }

    public long registerUser(User user) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            userDao.checkUniqueFields(con, user);
            long id = userDao.insertUser(con, user);
            daoFactory.commit(con);
            return id;
        } catch (InputErrorsMessagesException | UnknownSqlException e) {
            e.printStackTrace();
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.close(con);
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
        Connection con = null;
        try {
            con = daoFactory.open();
            return userDao.findUserById(con, id);
        } finally {
            daoFactory.close(con);
        }
    }

    public List<User> getUsersByRole(Role role) {
        Connection con = null;
        try {
            con = daoFactory.open();
            return userDao.findAllByRole(con, role);
        } finally {
            daoFactory.close(con);
        }
    }
}
