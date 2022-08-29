package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Role;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import com.yaroslav.lobur.validator.UserValidator;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    private static UserService userService;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        userService = new UserService();
        MySqlDatasource.resetDatabase();
    }

    @Test
    @Order(1)
    void registerUser() {
        User user = userService.getUserById(1);
        user.setId(0);
        user.setPassword("Password1");
        assertEquals(0, UserValidator.getInstance().validate(user).size());
        assertEquals(8, UserValidator.getInstance().validate(new User()).size());
        user.setPassword(PasswordEncryptor.getSHA1String(user.getPassword()));
        assertThrows(InputErrorsMessagesException.class, () -> userService.registerUser(user));
        user.setPhone("(000)-000-00-00");
        user.setEmail("test@gg.com");
        user.setLogin("test");
        long id = userService.registerUser(user);
        User user1 = userService.getUserById(id);
        user.setId(id);
        assertEquals(user, user1);
    }

    @Test
    @Order(2)
    void signIn() {
        User user = userService.getUserById(1);
        user.setPassword("Password1");
        long id = userService.signIn(user.getEmail(), user.getPassword());
        assertEquals(user.getId(), id);
        user.setPassword("WrongPassword1");
        assertThrows(DBExceptionMessages.class, () -> userService.signIn(user.getEmail(), user.getPassword()));
        user.setPassword("Password1");
        user.setEmail("notRegistered@email.com");
        assertThrows(DBExceptionMessages.class, () -> userService.signIn(user.getEmail(), user.getPassword()));
    }

    @Test
    @Order(3)
    void getUserById() {
        User user = new User();
        user.setId(6);
        user.setEmail("test@gg.com");
        assertThrows(EntityNotFoundException.class, () ->  userService.getUserById(27));
        assertEquals(user.getEmail(), userService.getUserById(user.getId()).getEmail());
        assertEquals(user.getId(), userService.getUserById(user.getId()).getId());
    }

    @Test
    @Order(4)
    void getUsersByRole() {
        List<User> users = userService.getUsersByRole(Role.DOCTOR);
        long c = users.stream().filter(u -> u.getRole().getRoleId() == 2).count();
        assertEquals(users.size(), c);
    }
}