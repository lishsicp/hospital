package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    UserValidator userValidator;
    User user;

    @BeforeEach
    void setUp() {
        userValidator = UserValidator.getInstance();
        user = new User();
        user.setLogin("test");
        user.setPassword("Password1");
        user.setFirstname("test");
        user.setLastname("test");
        user.setDateOfBirth(new Date());
        user.setGender("test");
        user.setEmail("test@tt.tt");
        user.setPhone("(000)-000-00-00");
    }

    @Test
    void validate() {
        assertEquals(8, userValidator.validate(new User()).size());
        assertEquals(0, userValidator.validate(user).size());
    }
}