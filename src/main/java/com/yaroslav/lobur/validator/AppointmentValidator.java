package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.utils.managers.RegexpManager;

import java.util.Map;

public class AppointmentValidator implements Validator<Appointment> {

    private static AppointmentValidator instance;

    private AppointmentValidator() {
    }

    public static AppointmentValidator getInstance() {
        if (instance == null) {
            instance = new AppointmentValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validate(Appointment entity) {
        var errors = HospitalCardValidator.getInstance().validate(entity.getHospitalCard());
        if (entity.getTitle() == null || !entity.getTitle().matches(RegexpManager.getProperty("appointment.title"))) {
            errors.put("description", "validation.appointment.desc");
        }
        if (entity.getType() == null) {
            errors.put("type", "validation.appointment.type");
        }
        return errors;
    }
}
