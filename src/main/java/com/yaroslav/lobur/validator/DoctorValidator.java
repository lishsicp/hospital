package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Doctor;

import java.util.Map;

public class DoctorValidator implements Validator<Doctor> {

    private static DoctorValidator instance;

    private DoctorValidator() {
    }

    public static DoctorValidator getInstance() {
        if (instance == null) {
            instance = new DoctorValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validate(Doctor doctor) {
        Map<String, String> errors = UserValidator.getInstance().validate(doctor.getUser());
        if (doctor.getCategory().getId() <= 0) {
            errors.put("category", "validation.doctor.category");
        }
        return errors;
    }
}
