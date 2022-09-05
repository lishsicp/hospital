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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class UserServiceTest {

    @Mock
    DaoFactory mockDaoFactory;
    @Mock
    Connection mockConn;
    @Mock
    UserDao mockUserDao;
    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    public void setUp() throws Exception{
        try(var ignored = MockitoAnnotations.openMocks(this)) {
            when(mockDaoFactory.open()).thenReturn(mockConn);
            when(mockDaoFactory.beginTransaction()).thenReturn(mockConn);
            doNothing().when(mockDaoFactory).commit(mockConn);
            doNothing().when(mockDaoFactory).rollback(mockConn);
            doNothing().when(mockDaoFactory).close(mockConn);
            user = new User();
            user.setId(1);
            user.setPassword(PasswordEncryptor.getSHA1String("password"));
            user.setFirstname("Name");
            user.setLastname("Surname");
            user.setEmail("email");
        }
    }

    @Test
    void testRegisterUserWithNoExceptions() {
        when(mockUserDao.insertUser(mockDaoFactory.beginTransaction(), user)).thenReturn(user.getId());
        assertEquals(user.getId(), userService.registerUser(user));
    }

    @Test
    void testRegisterUserWithExceptions() {
        doThrow(InputErrorsMessagesException.class).when(mockUserDao).checkUniqueFields(mockConn, user);
        when(mockUserDao.insertUser(mockDaoFactory.open(), user)).thenReturn(1L);
        assertThrows(InputErrorsMessagesException.class, () -> userService.registerUser(user));
        doNothing().when(mockDaoFactory).close(mockConn);
    }

    @Test
    void testSignInWithoutExceptions() {
        when(mockUserDao.findUserByEmail(mockDaoFactory.open(), "email")).thenReturn(user);
        assertEquals(user.getId(), userService.signIn("email", "password"));
    }

    @Test
    void testSignInWithExceptions() {
        when(mockUserDao.findUserByEmail(mockDaoFactory.open(), "wrong_email")).thenThrow(EntityNotFoundException.class);
        assertThrows(DBExceptionMessages.class, () -> userService.signIn("wrong_email", "password"));
        when(mockUserDao.findUserByEmail(mockDaoFactory.open(), "wrong_email2")).thenThrow(UnknownSqlException.class);
        assertThrows(UnknownSqlException.class, () -> userService.signIn("wrong_email2", "password"));
        when(mockUserDao.findUserByEmail(mockDaoFactory.open(), "email")).thenReturn(user);
        assertThrows(DBExceptionMessages.class, () -> userService.signIn("email", "wrong_password"));
    }

    @Test
    void testGetUserById() {
        when(mockUserDao.findUserById(mockDaoFactory.open(), user.getId())).thenReturn(user);
        assertEquals(user, userService.getUserById(user.getId()));
        doNothing().when(mockDaoFactory).close(mockConn);
    }

    @Test
    void testGetUsersByRole() {
        User user1 = new User();
        User user2 = new User();
        user1.setRole(Role.DOCTOR);
        user2.setRole(Role.ADMIN);
        List<User> users = List.of(user1, user2);
        when(mockUserDao.findAllByRole(mockDaoFactory.open(), Role.DOCTOR)).thenReturn(users);
        assertEquals(users.get(0), userService.getUsersByRole(Role.DOCTOR).get(0));
        doNothing().when(mockDaoFactory).close(mockConn);
    }
}