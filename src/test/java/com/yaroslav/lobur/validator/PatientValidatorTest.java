package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PatientValidatorTest {

    PatientValidator patientValidator;
    Patient patient;

    @BeforeEach
    void setUp() {
        patientValidator = PatientValidator.getInstance();
        patient = new Patient();
        patient.setId(1);
        patient.setEmail("test@tt.tt");
        patient.setDateOfBirth(new Date());
        patient.setGender("test");
        patient.setFirstname("test");
        patient.setLastname("test");
    }

    @Test
    void validate() {
        assertEquals(5, patientValidator.validate(new Patient()).size());
        assertEquals(0, patientValidator.validate(patient).size());
        patient.setEmail("test");
        assertEquals("validation.user.email", patientValidator.validate(patient).get("email"));
    }
}