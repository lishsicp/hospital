package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Entity;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.Gender;
import com.yaroslav.lobur.utils.RegexpManager;

import java.util.Date;
import java.util.Map;

public interface Validator<E extends Entity> {
    Map<String, String> validate(E entity);

    static void validateUserFields(Map<String, String> map, String firstname, String lastname, Date dateOfBirth, String gender, String email) {
        if (firstname == null || firstname.trim().isEmpty() || !firstname.matches(RegexpManager.getProperty("user.first_name"))) {
            map.put("firstName", "validation.user.first_name");
        }
        if (lastname == null || lastname.trim().isEmpty() || !lastname.matches(RegexpManager.getProperty("user.last_name"))) {
            map.put("lastName", "validation.user.last_name");
        }
        if (dateOfBirth == null || dateOfBirth.compareTo(new Date()) > 0) {
            map.put("date_of_birth", "validation.user.date_of_birth");
        }
        if (gender == null) {
            map.put("gender", "validation.user.gender");
        }
        if (email == null || !email.matches(RegexpManager.getProperty("user.email.regexp"))) {
            map.put("email", "validation.user.email");
        }
    }
}
