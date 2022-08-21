package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.utils.RegexpManager;

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
    public Map<String, String> validate(User entity) {
        Map<String, String> map = new HashMap<>();
        if (entity.getLogin() == null || !entity.getLogin().matches(RegexpManager.getProperty("user.login.regexp"))) {
            map.put("login", "validation.user.login");
        }
        if (entity.getPassword() == null || !entity.getPassword().matches(RegexpManager.getProperty("user.password.regexp"))) {
            map.put("psw", "validation.user.password");
        }
        Validator.validateUserFields(map, entity.getFirstname(), entity.getLastname(), entity.getDateOfBirth(), entity.getGender(), entity.getEmail());
        if (entity.getPhone() == null || !entity.getPhone().matches(RegexpManager.getProperty("user.phone.regexp"))) {
            map.put("phone", "validation.user.phone");
        }
        return map;
    }
}
