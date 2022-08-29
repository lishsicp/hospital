package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.validator.HospitalCardValidator;
import com.yaroslav.lobur.validator.PatientValidator;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientServiceTest {

    static PatientService patientService;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        DaoFactory.init(MySqlDatasource.getDataSource());
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
        assertEquals(list.size(), patientService.getNumberOfRecordsHC());
    }

    @Test
    void getAllPatientsSorted() {
        List<Patient> patients = patientService.getAllPatientsSorted(OrderBy.NAME, 0, 100);
        List<Patient> patientsSorted = new ArrayList<>(patients);
        patientsSorted.sort(Comparator.comparing(Patient::getLastname));
        assertEquals(patients, patientsSorted);
    }

    @Test
    void getPatientsWithoutDoctor() {
        List<Patient> patients = patientService.getPatientsWithoutDoctor(OrderBy.NAME, 0, 100);
        assertNull(patients.get(0).getDoctor());
    }

    @Test
    @Order(1)
    void addPatient() {
        Patient validPatient;
        validPatient = patientService.getPatientById(1);
        Patient invalidPatient = validPatient;
        assertEquals(invalidPatient.getId(), validPatient.getId());
        invalidPatient.setId(0);
        assertThrows(InputErrorsMessagesException.class, () -> patientService.addPatient(invalidPatient));
        validPatient.setEmail("test@gg.com");
        assertEquals(0, PatientValidator.getInstance().validate(validPatient).size());
        assertEquals(5, PatientValidator.getInstance().validate(new Patient()).size());
        patientService.addPatient(validPatient);
        invalidPatient.setEmail("test");
        assertEquals(1, PatientValidator.getInstance().validate(invalidPatient).size());
    }

    @Test
    @Order(2)
    void updatePatient() {
        Patient patient = patientService.getPatientById(3);
        patient.setLastname("Test");
        patientService.updatePatient(patient);
        assertEquals(patient.getLastname(), patientService.getPatientById(3).getLastname());
        patient.setEmail("test@gg.com");
        assertThrows(InputErrorsMessagesException.class, () -> patientService.updatePatient(patient));
        patient.setEmail("wrong email");
        assertTrue(PatientValidator.getInstance().validate(patient).containsKey("email"));
    }


    @Test
    @Order(3)
    void deletePatient() {
        var before = patientService.getAllPatients();
        patientService.deletePatient(4);
        assertEquals(before.size() - 1, patientService.getAllPatients().size());
    }

    @Test
    void updateHospitalCard() {
        HospitalCard hospitalCard = patientService.getHospitalCardById(1);
        assertEquals(0, HospitalCardValidator.getInstance().validate(hospitalCard).size());
        hospitalCard.setDiagnosis("Test");
        patientService.updateHospitalCard(hospitalCard);
        assertEquals(hospitalCard.getDiagnosis(), patientService.getHospitalCardById(1).getDiagnosis());
        hospitalCard.setDiagnosis(null);
        assertEquals(1, HospitalCardValidator.getInstance().validate(hospitalCard).size());
    }


}