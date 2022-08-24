package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.utils.RegexpManager;

import java.util.HashMap;
import java.util.Map;

public class HospitalCardValidator implements Validator<HospitalCard> {

    private static HospitalCardValidator instance;

    private HospitalCardValidator() {
    }

    public static HospitalCardValidator getInstance() {
        if (instance == null) {
            instance = new HospitalCardValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validate(HospitalCard hospitalCard) {
        Map<String, String> errors = new HashMap<>();
        if (hospitalCard.getDiagnosis() == null || hospitalCard.getDiagnosis().trim().isEmpty() || !hospitalCard.getDiagnosis().matches(RegexpManager.getProperty("user.first_name"))) {
            errors.put("diagnosis", "validation.hospitalCard.diagnosis");
        }
        return errors;
    }
}