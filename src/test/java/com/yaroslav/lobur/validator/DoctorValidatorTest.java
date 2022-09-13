package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorValidatorTest {

    @Mock
    UserValidator mockUserValidator = mock(UserValidator.class);

    DoctorValidator doctorValidator;
    Doctor doctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorValidator = DoctorValidator.getInstance();
        doctor = new Doctor();
        doctor.setId(1);
        Category category = new Category();
        category.setId(1);
        User user = new User();
        doctor.setCategory(category);
        doctor.setUser(user);
    }

    @Test
    void testValidate() {
        try (MockedStatic<UserValidator> staticInstance = mockStatic(UserValidator.class)) {
            staticInstance.when(UserValidator::getInstance).thenReturn(mockUserValidator);
            assertEquals(0, doctorValidator.validate(doctor).size());
            doctor.getCategory().setId(0);
            assertEquals(1, doctorValidator.validate(doctor).size());
            staticInstance.verify(UserValidator::getInstance, times(2));
        }
    }
}