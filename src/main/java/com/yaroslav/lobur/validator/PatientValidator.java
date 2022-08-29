package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.utils.RegexpManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PatientValidator implements Validator<Patient> {

    private static PatientValidator instance;

    private PatientValidator() {
    }

    public static PatientValidator getInstance() {
        if (instance == null) {
            instance = new PatientValidator();
        }
        return instance;
    }

    public Map<String, String> validate(Patient entity) {
        Map<String, String> map = new HashMap<>();
        if (entity.getFirstname() == null || entity.getFirstname().trim().isEmpty() || !entity.getFirstname().matches(RegexpManager.getProperty("user.first_name"))) {
            map.put("firstName", "validation.user.first_name");
        }
        if (entity.getLastname() == null || entity.getLastname().trim().isEmpty() || !entity.getLastname().matches(RegexpManager.getProperty("user.last_name"))) {
            map.put("lastName", "validation.user.last_name");
        }
        if (entity.getDateOfBirth() == null || entity.getDateOfBirth().compareTo(new Date()) > 0) {
            map.put("date_of_birth", "validation.user.date_of_birth");
        }
        if (entity.getGender() == null) {
            map.put("gender", "validation.user.gender");
        }
        if (entity.getEmail() == null || !entity.getEmail().matches(RegexpManager.getProperty("user.email.regexp"))) {
            map.put("email", "validation.user.email");
        }
        return map;
    }
}
