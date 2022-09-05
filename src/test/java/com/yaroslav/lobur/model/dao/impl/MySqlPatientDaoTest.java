package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySqlPatientDaoTest {

    DataSource dataSource = MySqlDatasource.getDataSource();
    DaoFactory daoFactory = new MySqlDaoFactory(dataSource);
    PatientDao patientDao = daoFactory.getPatientDao();

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        MySqlDatasource.resetDatabase();
    }

    @Test
    void findPatientById_ThrowsException_IfPatientDoNotExist() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> patientDao.findPatientById(con, 1000));
            con.close();
            assertThrows(UnknownSqlException.class, () -> patientDao.findPatientById(con, 1));
        }
    }

    @Test
    void checkUniqueEmail_ThrowsException_IfEmailExist() throws SQLException {
        Patient patient = new Patient();
        patient.setEmail("ivan2000@gmail.com");
        try (Connection con = daoFactory.open()) {
            assertThrows(InputErrorsMessagesException.class, () -> patientDao.checkUniqueEmail(con, patient));
        }
    }

    @Test
    void findPatientsWithoutDoctor_ReturnsOnePatient_WithoutDoctorAssigned() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Patient> patientList = patientDao.findPatientsWithoutDoctor(con, OrderBy.NAME, 0, 5);
            assertEquals(1, patientList.size());
            con.close();
            assertThrows(UnknownSqlException.class, () -> patientDao.findPatientsWithoutDoctor(con, OrderBy.NAME, 0, 100));
        }
    }

    @Test
    void findPatientsOrderBy_ReturnsThreePatients_SortedByLastName() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Patient> patients = patientDao.findPatientsOrderBy(con, OrderBy.NAME, 0, 100);
            List<Patient> patientsSorted = new ArrayList<>(patients);
            patientsSorted.sort((p1, p2) -> p1.getLastname().compareToIgnoreCase(p2.getLastname()));
            assertEquals(patientsSorted, patients);
            con.close();
            assertThrows(UnknownSqlException.class, () -> patientDao.findPatientsOrderBy(con, OrderBy.NAME, 0, 100));
        }
    }

    @Test
    void findAllPatients_ReturnsThreePatients() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Patient> patients = patientDao.findAllPatients(con);
            assertEquals(3, patients.size());
            con.close();
            assertThrows(UnknownSqlException.class, ()-> patientDao.findAllPatients(con));
        }
    }

    @Test
    @Order(1)
    void insertPatient_ReturnsPatientId_InsertsPatientToDatabase() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Patient patient = patientDao.findPatientById(con, 1);
            patient.setEmail("example@gmail.com");
            long id = patientDao.insertPatient(con, patient);
            Patient insertedPatient = patientDao.findPatientById(con, id);
            assertEquals(patient.getEmail(), insertedPatient.getEmail());
            con.close();
            assertThrows(UnknownSqlException.class, () -> patientDao.insertPatient(con, patient));
        }
    }

    @Test
    @Order(2)
    void deletePatient_DeletesPatientFromDatabase() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Patient> patients = patientDao.findAllPatients(con);
            patientDao.deletePatient(con, 4);
            List<Patient> patientsAfterDeleting = patientDao.findAllPatients(con);
            assertEquals(patients.size() - 1, patientsAfterDeleting.size());
            con.close();
            assertThrows(UnknownSqlException.class, () -> patientDao.deletePatient(con, 2));
        }
    }

    @Test
    @Order(3)
    void updatePatient_UpdatesPatient() throws SQLException {
        try (Connection con = daoFactory.beginTransaction()) {
            Patient patientToUpdate = patientDao.findPatientById(con, 1);
            String name = "Updated";
            patientToUpdate.setLastname(name);
            patientDao.updatePatient(con, patientToUpdate);
            Patient updatedPatient = patientDao.findPatientById(con, 1);
            assertEquals(patientToUpdate.getLastname(), updatedPatient.getLastname());
            con.close();
            assertThrows(UnknownSqlException.class, () -> patientDao.updatePatient(con, patientToUpdate));
        }
    }

    @Test
    void getNumberOfRecords_Three_CountAllPatients() throws SQLException {
        try (Connection con = daoFactory.beginTransaction()) {
            patientDao.findAllPatients(con);
            assertEquals(3, patientDao.getNumberOfRecords());
        }
    }

}