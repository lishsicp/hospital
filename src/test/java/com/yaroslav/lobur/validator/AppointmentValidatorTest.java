package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentValidatorTest {

    private final AppointmentValidator appointmentValidator = AppointmentValidator.getInstance();
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointment = new Appointment();
        appointment.setTitle("test");
        appointment.setType(AppointmentType.MEDICATION);
    }

    @Test
    void validate() {
        assertEquals(0, appointmentValidator.validate(appointment).size());
        appointment.setTitle("123");
        assertEquals(1, appointmentValidator.validate(appointment).size());
        appointment.setType(null);
        assertEquals(2, appointmentValidator.validate(appointment).size());
    }
}