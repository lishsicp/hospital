package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientServiceTest {

    static PatientService patientService;
    static DaoFactory daoFactory;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        daoFactory = DaoFactory.getDaoFactory();
        patientService = new PatientService();
        MySqlDatasource.resetDatabase();
    }

    @Test
    void getPatientById() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setEmail("varvar2000@gmail.com");
        assertThrows(EntityNotFoundException.class, () ->  patientService.getPatientById(27));
        assertEquals(patient.getEmail(), patientService.getPatientById(patient.getId()).getEmail());
        assertEquals(patient.getId(), patientService.getPatientById(patient.getId()).getId());
    }

    @Test
    void getHospitalCardById() {
        assertEquals("Ангіна", patientService.getHospitalCardById(2).getDiagnosis());
        assertThrows(EntityNotFoundException.class, () -> patientService.getHospitalCardById(333));
    }

    @Test
    void getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        assertEquals(3, patients.size());
        assertEquals(patientService.getNumberOfRecords(), patients.size());
    }

    @Test
    void getAllHospitalCardSorted() {
        long doctorId = 1;
        List<HospitalCard> list = patientService.getAllHospitalCardSortedByDoctorId(doctorId, 0, 100);
        assertEquals(doctorId, list.get(0).getPatient().getDoctor().getId());
    }

    @Test
    void getAllPatientsSorted() {
    }

    @Test
    void getPatientsWithoutDoctor() {
        List<Patient> patients = patientService.getPatientsWithoutDoctor(OrderBy.NAME, 0, 100);
        assertNull(patients.get(0).getDoctor());
    }

    @Test
    void addPatient() {
        Patient validPatient = new Patient();
        //assertEquals();
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

}