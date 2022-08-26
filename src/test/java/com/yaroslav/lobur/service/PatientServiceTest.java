package com.yaroslav.lobur.service;

import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.Patient;
import db.MySqlDatasource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PatientServiceTest {

    PatientService patientService;

    @BeforeEach
    void setUp() throws SQLException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        patientService = new PatientService();
        //when(patientService.getPatientById(25)).thenReturn(25);
    }

    @Test
    void getPatientById() {
        Patient patient = patientService.getPatientById(27);
        assertEquals(27, patient.getId());
    }

    @Test
    void getHospitalCardById() {
    }

    @Test
    void getAllPatients() {
    }

    @Test
    void getAllHospitalCardSorted() {
    }

    @Test
    void getAllPatientsSorted() {
    }

    @Test
    void getPatientsWithoutDoctor() {
    }

    @Test
    void addPatient() {
    }

    @Test
    void deletePatient() {
    }

    @Test
    void updatePatient() {
    }

    @Test
    void updateHospitalCard() {
    }

    @Test
    void getNumberOfRecords() {
    }

    @Test
    void getNumberOfRecordsHC() {
    }

    @AfterAll
    static void destroy() throws SQLException {
        MySqlDatasource.getDataSource();
    }
}