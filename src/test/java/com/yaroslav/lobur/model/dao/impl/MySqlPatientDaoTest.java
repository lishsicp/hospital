package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Patient;
import db.MySqlDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlPatientDaoTest {

    PatientDao patientDao;
    DaoFactory daoFactory;

    @BeforeEach
    void setUp() throws SQLException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        daoFactory = DaoFactory.getDaoFactory();
        patientDao = daoFactory.getPatientDao();
    }

    @Test
    void checkUniqueEmail() throws SQLException {
        Patient patient = new Patient();
        patient.setEmail("serhii@ukr.net");
        try (Connection con = daoFactory.open()) {
            assertThrows(InputErrorsMessagesException.class, () -> patientDao.checkUniqueEmail(con, patient));
        }
    }

    @Test
    void findPatientById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> patientDao.findPatientById(con, 100));
        }
    }
}