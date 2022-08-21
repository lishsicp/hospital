package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    List<User> findAllUsers(Connection connection);
    List<User> findAllByRole(Connection connection, Role role);
    User findUserById(Connection connection, Long id);

    User findUserByEmail(Connection connection, String email);
    void updateUser(Connection connection, User user);
    void deleteUser(Connection connection, long id);
    long insertUser(Connection connection, User user);

    void updatePassword(Connection connection, User user);

    void checkUniqueFields(Connection connection, User user);
}


