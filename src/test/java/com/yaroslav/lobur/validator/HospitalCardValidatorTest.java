package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.HospitalCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


class HospitalCardValidatorTest {

    HospitalCardValidator hospitalCardValidator;
    HospitalCard hospitalCard;

    @BeforeEach
    void setUp() {
        hospitalCardValidator = HospitalCardValidator.getInstance();
        hospitalCard = new HospitalCard();
    }

    @Test
    void testHospitalCardValidator() {
        assertEquals(1, hospitalCardValidator.validate(hospitalCard).size());
        hospitalCard.setDiagnosis("test");
        assertEquals(0, hospitalCardValidator.validate(hospitalCard).size());
    }
}