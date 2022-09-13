package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.DoctorDao;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySqlDoctorDaoTest {

    DataSource dataSource = MySqlDatasource.getDataSource();
    DaoFactory daoFactory = new MySqlDaoFactory(dataSource);
    DoctorDao doctorDao = daoFactory.getDoctorDao();

    @AfterAll
    static void cleanUp() throws SQLException, FileNotFoundException {
        MySqlDatasource.resetDatabase();
    }

    @Test
    void testFindAllDoctors() throws SQLException {
        try (Connection con = daoFactory.open()) {
            doctorDao.findAllDoctors(con);
            assertEquals(3, doctorDao.getNumberOfRecords());
        }
    }

    @Test
    void testFindDoctorsOrderBy() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Doctor> doctorList = doctorDao.findDoctorsOrderBy(con, OrderBy.NUMBER_OF_PATIENTS, 0, 5);
            List<Doctor> doctorListSorted = new ArrayList<>(doctorList);
            doctorListSorted.sort(Comparator.comparingLong(Doctor::getNumberOfPatients).reversed());
            assertEquals(doctorListSorted, doctorList);
        }
    }

    @Test
    void testFindDoctorsByCategory() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Doctor> doctorList = doctorDao.findDoctorsByCategory(con, 1);
            assertEquals(1, doctorList.get(0).getCategory().getId());
        }
    }

    @Test
    void testFindDoctorById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> doctorDao.findDoctorById(con, 1000));
            Doctor doctor = doctorDao.findDoctorById(con, 1);
            assertEquals(doctor, doctorDao.findDoctorById(con, 1));
            assertEquals(doctor.toString(), doctorDao.findDoctorById(con, 1).toString());
        }
    }

    @Test
    void testFindDoctorByUserId() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> doctorDao.findDoctorByUserId(con, 1000));
        }
    }

    @Test
    void testInsertDoctor() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Doctor doctor = doctorDao.findDoctorById(con, 1);
            User user = new User();
            user.setId(1);
            Category category = new Category();
            category.setId(1);
            doctor.setCategory(category);
            doctor.setUser(user);
            long id = doctorDao.insertDoctor(con, doctor);
            assertEquals(4, id);
        }
    }
}