package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Patient;
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
        Validator.validateUserFields(map, entity.getFirstname(), entity.getLastname(), entity.getDateOfBirth(), entity.getGender(), entity.getEmail());
        return map;
    }
}
