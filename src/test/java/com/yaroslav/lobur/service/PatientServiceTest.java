package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.HospitalCardDao;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    DaoFactory mockDaoFactory;
    @Mock
    Connection mockConn;
    @Mock
    PatientDao mockPatientDao;
    @Mock
    HospitalCardDao mockHospitalCardDao;
    @InjectMocks
    PatientService patientService;

    Patient patient;
    HospitalCard hospitalCard;

    @BeforeEach
    void setUp() throws Exception {
        try(var ignored = MockitoAnnotations.openMocks(this)) {
            when(mockDaoFactory.open()).thenReturn(mockConn);
            when(mockDaoFactory.beginTransaction()).thenReturn(mockConn);
            when(mockPatientDao.getNumberOfRecords()).thenReturn(1);
            when(mockHospitalCardDao.getNumberOfRecords()).thenReturn(1);
            doNothing().when(mockDaoFactory).commit(mockConn);
            doNothing().when(mockDaoFactory).rollback(mockConn);
            doNothing().when(mockDaoFactory).close(mockConn);
            patient = new Patient();
            patient.setId(1);
            patient.setFirstname("Name");
            patient.setLastname("Surname");
            patient.setEmail("email");
            patient.setStatus(PatientStatus.TREATMENT);
            Doctor doctor = new Doctor();
            doctor.setId(1);
            patient.setDoctor(doctor);

            hospitalCard = new HospitalCard();
            hospitalCard.setPatient(patient);
            hospitalCard.setId(1);
            hospitalCard.setDiagnosis("test");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetPatientById() {
        when(mockPatientDao.findPatientById(mockDaoFactory.open(), patient.getId())).thenReturn(patient);
        assertEquals(patient, patientService.getPatientById(patient.getId()));
    }

    @Test
    void testGetHospitalCardById() {
        when(mockHospitalCardDao.findHospitalCardById(mockDaoFactory.open(), patient.getId())).thenReturn(hospitalCard);
        assertEquals(hospitalCard, patientService.getHospitalCardById(hospitalCard.getId()));
    }

    @Test
    void testGetAllPatients() {
        var patients = List.of(patient);
        when(mockPatientDao.findAllPatients(mockDaoFactory.open())).thenReturn(patients);
        assertEquals(patients, patientService.getAllPatients());
        assertEquals(patients.size(), patientService.getNumberOfRecords());
    }

    @Test
    void testGetAllHospitalCardSortedByDoctorId() {
        when(mockHospitalCardDao.findAllHospitalCardsForDoctor(mockDaoFactory.open(), patient.getDoctor().getId(), 0, 5)).thenReturn(List.of(hospitalCard));
        assertEquals(List.of(hospitalCard), patientService.getAllHospitalCardSortedByDoctorId(patient.getDoctor().getId(), 0, 5));
        assertEquals(1, patientService.getNumberOfRecordsHC());
    }

    @Test
    void testGetAllPatientsSorted() {
        when(mockPatientDao.findPatientsOrderBy(mockDaoFactory.open(), OrderBy.NAME, 0, 100)).thenReturn(List.of(patient));
        assertEquals(1, patientService.getAllPatientsSorted(OrderBy.NAME, 0, 100).size());
    }

    @Test
    void testGetPatientsWithoutDoctor() {
        when(mockPatientDao.findPatientsWithoutDoctor(mockDaoFactory.open(), OrderBy.NAME, 0, 100)).thenReturn(List.of(patient));
        assertEquals(1, patientService.getPatientsWithoutDoctor(OrderBy.NAME, 0, 100).size());
    }

    @Test
    void testGetAddPatientWithoutExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            when(mockPatientDao.insertPatient(connection, patient)).thenReturn(patient.getId());
            when(mockHospitalCardDao.insertHospitalCard(connection, hospitalCard)).thenReturn(hospitalCard.getId());
            doNothing().when(mockPatientDao).checkUniqueEmail(connection, patient);
            assertDoesNotThrow(()-> patientService.addPatient(patient));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testGetAddPatientWithExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doThrow(InputErrorsMessagesException.class).when(mockPatientDao).checkUniqueEmail(connection, patient);
            when(mockPatientDao.insertPatient(connection, patient)).thenReturn(patient.getId());
            when(mockHospitalCardDao.insertHospitalCard(connection, hospitalCard)).thenReturn(hospitalCard.getId());
            assertThrows(InputErrorsMessagesException.class, () -> patientService.addPatient(patient));
            doThrow(UnknownSqlException.class).when(mockPatientDao).checkUniqueEmail(connection, patient);
            assertThrows(UnknownSqlException.class, () -> patientService.addPatient(patient));
        } finally {
            mockDaoFactory.rollback(mockConn);
        }
    }

    @Test
    void testDeletePatientWithNoExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doNothing().when(mockPatientDao).deletePatient(connection, patient.getId());
            assertDoesNotThrow(() -> patientService.deletePatient(patient.getId()));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testDeletePatientWithExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            long id = patient.getId();
            doThrow(UnknownSqlException.class).when(mockPatientDao).deletePatient(connection, patient.getId());
            assertThrows(UnknownSqlException.class, () -> patientService.deletePatient(id));
            doThrow(DBExceptionMessages.class).when(mockPatientDao).deletePatient(connection, patient.getId());
            assertThrows(DBExceptionMessages.class, () -> patientService.deletePatient(id));
        }
    }

    @Test
    void testUpdatePatientWithNoExceptions() throws SQLException {
        Patient patient1 = new Patient();
        patient1.setEmail("email");
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            when(mockPatientDao.findPatientById(connection, patient1.getId())).thenReturn(patient);
            doNothing().when(mockPatientDao).updatePatient(connection, patient1);
            assertDoesNotThrow(() -> patientService.updatePatient(patient1));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testUpdatePatientWithExceptions() throws SQLException {
        Patient patient1 = new Patient();
        patient1.setId(1);
        patient1.setEmail("wrong");
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            when(mockPatientDao.findPatientById(connection, patient1.getId())).thenReturn(patient);
            doNothing().when(mockPatientDao).updatePatient(connection, patient1);
            doThrow(InputErrorsMessagesException.class).when(mockPatientDao).checkUniqueEmail(connection, patient1);
            assertThrows(InputErrorsMessagesException.class, () -> patientService.updatePatient(patient1));

            doThrow(DBExceptionMessages.class).when(mockPatientDao).findPatientById(connection, patient1.getId());
            assertThrows(DBExceptionMessages.class, () -> patientService.updatePatient(patient1));

            doThrow(UnknownSqlException.class).when(mockPatientDao).findPatientById(connection, patient1.getId());
            assertThrows(UnknownSqlException.class, () -> patientService.updatePatient(patient1));

            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testUpdateHospitalCardWithNoExceptions() throws SQLException {
        HospitalCard hospitalCard1 = new HospitalCard();
        hospitalCard1.setDiagnosis("test");
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doNothing().when(mockHospitalCardDao).updateHospitalCard(connection, hospitalCard1);
            assertDoesNotThrow(() -> patientService.updateHospitalCard(hospitalCard1));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testUpdateHospitalCardWithExceptions() throws SQLException {
        HospitalCard hospitalCard1 = new HospitalCard();
        hospitalCard1.setDiagnosis("test");
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doThrow(UnknownSqlException.class).when(mockHospitalCardDao).updateHospitalCard(connection, hospitalCard1);
            assertThrows(UnknownSqlException.class, () -> patientService.updateHospitalCard(hospitalCard1));

            doThrow(DBExceptionMessages.class).when(mockHospitalCardDao).updateHospitalCard(connection, hospitalCard1);
            assertThrows(DBExceptionMessages.class, () -> patientService.updateHospitalCard(hospitalCard1));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }
}