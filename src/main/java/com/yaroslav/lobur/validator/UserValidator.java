package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.utils.RegexpManager;

import java.util.*;

public class UserValidator implements Validator<User> {

    @Override
    public Map<String, String> validate(User entity) {
        Map<String, String> map = new HashMap<>();
        if (entity.getLogin() == null || !entity.getLogin().matches(RegexpManager.getProperty("user.login.regexp"))) {
            map.put("login", "validation.user.login");
        }
        if (entity.getPassword() == null || !entity.getPassword().matches(RegexpManager.getProperty("user.password.regexp"))) {
            map.put("psw", "validation.user.password");
        }
        Validator.validateUserFields(map, entity.getFirstname(), entity.getLastname(), entity.getDateOfBirth(), entity.getGender().toString(), entity.getEmail());
        if (entity.getPhone() == null || !entity.getPhone().matches(RegexpManager.getProperty("user.phone.regexp"))) {
            map.put("phone", "validation.user.phone");
        }
//        if (!entity.getAddress().isEmpty() && !entity.getAddress().isBlank()) {
//            map.put("address", "validation.user.address");
//        }
        return map;
    }
}
