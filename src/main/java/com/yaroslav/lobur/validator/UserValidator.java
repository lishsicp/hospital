package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.utils.managers.RegexpManager;

import java.util.*;

public class UserValidator implements Validator<User> {

    private static UserValidator instance;

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        if (instance == null) {
            instance = new UserValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validate(User user) {
        Map<String, String> map = new HashMap<>();
        if (user.getLogin() == null || !user.getLogin().matches(RegexpManager.getProperty("user.login.regexp"))) {
            map.put("login", "validation.user.login");
        }
        if (user.getPassword() == null || !user.getPassword().matches(RegexpManager.getProperty("user.password.regexp"))) {
            map.put("psw", "validation.user.password");
        }
        if (user.getFirstname() == null || user.getFirstname().trim().isEmpty() || !user.getFirstname().matches(RegexpManager.getProperty("user.first_name"))) {
            map.put("firstName", "validation.user.first_name");
        }
        if (user.getLastname() == null || user.getLastname().trim().isEmpty() || !user.getLastname().matches(RegexpManager.getProperty("user.last_name"))) {
            map.put("lastName", "validation.user.last_name");
        }
        if (user.getDateOfBirth() == null || user.getDateOfBirth().compareTo(new Date()) > 0) {
            map.put("date_of_birth", "validation.user.date_of_birth");
        }
        if (user.getGender() == null) {
            map.put("gender", "validation.user.gender");
        }
        if (user.getEmail() == null || !user.getEmail().matches(RegexpManager.getProperty("user.email.regexp"))) {
            map.put("email", "validation.user.email");
        }
        if (user.getPhone() == null || !user.getPhone().matches(RegexpManager.getProperty("user.phone.regexp"))) {
            map.put("phone", "validation.user.phone");
        }
        return map;
    }
}
