package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.HospitalCardDao;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySqlHospitalCardDaoTest {

    static DaoFactory daoFactory;
    static HospitalCardDao hospitalCardDao;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        daoFactory = DaoFactory.getDaoFactory();
        hospitalCardDao = daoFactory.getHospitalCardDao();
        MySqlDatasource.resetDatabase();
    }

    @Test
    @Order(1)
    void insertHospitalCard() throws SQLException {
        try (Connection con = daoFactory.open()) {
            HospitalCard hospitalCard = new HospitalCard();
            Patient p = new Patient();
            p.setId(1);
            hospitalCard.setPatient(p);
            hospitalCard.setDiagnosis("test");
            long id = hospitalCardDao.insertHospitalCard(con, hospitalCard);
            assertEquals(id, hospitalCardDao.findHospitalCardById(con, id).getId());
        }
    }

    @Test
    @Order(2)
    void updateHospitalCard() throws SQLException {
        try (Connection con = daoFactory.open()) {
            HospitalCard hospitalCard = hospitalCardDao.findHospitalCardById(con, 1);
            hospitalCard.setDiagnosis("test");
            hospitalCardDao.updateHospitalCard(con, hospitalCard);
            assertEquals(hospitalCard.getDiagnosis(), hospitalCardDao.findHospitalCardById(con, hospitalCard.getId()).getDiagnosis());
        }
    }

    @Test
    @Order(3)
    void findAllHospitalCards() throws SQLException {
        try (Connection con = daoFactory.open()) {
            hospitalCardDao.findAllHospitalCards(con);
            int hospitalCardCount = hospitalCardDao.getNumberOfRecords();
            assertEquals(4, hospitalCardCount);
        }
    }

    @Test
    @Order(4)
    void findAllHospitalCardsForDoctor() throws SQLException {
        try (Connection con = daoFactory.open()) {
            int hospitalCardForDoctorCount = hospitalCardDao.findAllHospitalCardsForDoctor(con, 1, 0, 100).size();
            assertEquals(1, hospitalCardForDoctorCount);
        }
    }

    @Test
    @Order(5)
    void findHospitalCardById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertEquals("test", hospitalCardDao.findHospitalCardById(con, 4).getDiagnosis());
        }
    }
}