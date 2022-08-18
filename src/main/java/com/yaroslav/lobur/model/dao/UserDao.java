package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.util.List;

public interface UserDao {
    List<User> findAllUsers();
    List<User> findAllByRole(Role role);
    User findUserById(Long id);

    User findUserByEmail(String email);
    void updateUser(User user);
    void deleteUser(long id);
    long insertUser(User user);

    void updatePassword(User user);

    void checkUniqueFields(User user);
}


