package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.Role;
import db.MySqlDatasource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySqlUserDaoTest {

    static DaoFactory daoFactory;
    static UserDao userDao;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        daoFactory = DaoFactory.getDaoFactory();
        userDao = daoFactory.getUserDao();
        MySqlDatasource.resetDatabase();
    }

    @Test
    void findAllUsers_ReturnsFiveUsers() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<User> users = userDao.findAllUsers(con);
            assertEquals(5, users.size());
        }
    }

    @Test
    void findAllByRole_ReturnsThreeUsers_IfUserRoleIsDoctor() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<User> users = userDao.findAllByRole(con, Role.DOCTOR);
            assertEquals(3, users.size());
        }
    }

    @Test
    void findUserById_ReturnUser_UserRoleIdOne()throws SQLException {
        try (Connection con = daoFactory.open()) {
            User user = userDao.findUserById(con, 1L);
            assertEquals(1, user.getRole().getRoleId());
        }
    }

    @Test
    void findUserByEmail_ReturnUser() throws SQLException {
        try (Connection con = daoFactory.open()) {
            String userEmail = "lobur13@gmail.com";
            assertEquals(userEmail, userDao.findUserByEmail(con, "lobur13@gmail.com").getEmail());
        }
    }

    @Test
    @Order(1)
    void insertUser() throws SQLException {
        try (Connection con = daoFactory.open()) {
            User user = userDao.findUserById(con, 2L);
            user.setEmail("test@email.com");
            user.setLogin("test");
            user.setPhone("(000)-000-00-00");
            user.setLocale(Locale.UK);
            long id = userDao.insertUser(con, user);
            assertEquals(id, userDao.findUserById(con, id).getId());
        }
    }

    @Test
    @Order(2)
    void updateUser() throws SQLException {
        try (Connection con = daoFactory.open()) {
            User user = userDao.findUserById(con, 6L);
            user.setEmail("test2@email.com");
            user.setLogin("test2");
            user.setPhone("(222)-000-00-00");
            user.setLocale(Locale.UK);
            userDao.updateUser(con, user);
            assertEquals(user, userDao.findUserById(con, 6L));
        }
    }

    @Test
    @Order(3)
    void deleteUser() throws SQLException {
        try (Connection con = daoFactory.open()) {
            int beforeDelete = userDao.findAllUsers(con).size();
            userDao.deleteUser(con, 6L);
            int afterDelete = userDao.findAllUsers(con).size();
            assertEquals(beforeDelete - 1, afterDelete);
        }
    }



    @Test
    void updatePassword() {
        assertThrows(UnsupportedOperationException.class, () -> userDao.updatePassword(null, null));
    }

    @Test
    void checkUniqueFields() throws SQLException {
        try (Connection con = daoFactory.open()) {
            User user = userDao.findUserById(con, 1L);
            userDao.checkUniqueFields(con, user);
        } catch (InputErrorsMessagesException e) {
            assertEquals(3, e.getErrorMessageMap().size());
        }
    }

    @AfterAll
    static void cleanUp() throws SQLException, FileNotFoundException {
        MySqlDatasource.resetDatabase();
    }
}