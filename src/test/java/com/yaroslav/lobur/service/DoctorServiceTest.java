package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;
import com.yaroslav.lobur.model.entity.*;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import org.checkerframework.checker.units.qual.C;
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
import static org.mockito.Mockito.doThrow;

class DoctorServiceTest {

    @Mock
    DaoFactory mockDaoFactory;
    @Mock
    Connection mockConn;
    @Mock
    DoctorDao mockDoctorDao;
    @Mock
    CategoryDao mockCategoryDao;
    @Mock
    UserDao mockUserDao;
    @InjectMocks
    DoctorService doctorService;

    Doctor doctor;
    User user;
    Category category;

    @BeforeEach
    void setUp() throws Exception {
        try(var ignored = MockitoAnnotations.openMocks(this)) {
            when(mockDaoFactory.open()).thenReturn(mockConn);
            when(mockDaoFactory.beginTransaction()).thenReturn(mockConn);
            when(mockDoctorDao.getNumberOfRecords()).thenReturn(1);
            doNothing().when(mockDaoFactory).commit(mockConn);
            doNothing().when(mockDaoFactory).rollback(mockConn);
            doNothing().when(mockDaoFactory).close(mockConn);
            doctor = new Doctor();
            doctor.setId(1);
            category = new Category();
            category.setId(1);
            category.setName("test");
            user = new User();
            user.setId(1);
            doctor.setUser(user);
        }
    }

    @Test
    void testGetAllDoctors() {
        var doctors = List.of(doctor);
        when(mockDoctorDao.findAllDoctors(mockDaoFactory.open())).thenReturn(doctors);
        assertEquals(doctors, doctorService.getAllDoctors());
        assertEquals(doctors.size(), doctorService.getNumberOfRecords());
    }

    @Test
    void testGetDoctorsByCategory() {
        long categoryId = category.getId();
        var doctors = List.of(doctor);
        when(mockDoctorDao.findDoctorsByCategory(mockDaoFactory.open(), categoryId)).thenReturn(doctors);
        assertEquals(doctors, doctorService.getDoctorsByCategory(categoryId));
        assertEquals(doctors.size(), doctorService.getNumberOfRecords());
    }

    @Test
    void testGetAllDoctorsOrderBy() {
        var doctors = List.of(doctor);
        when(mockDoctorDao.findDoctorsOrderBy(mockDaoFactory.open(), OrderBy.NAME, 0, 99)).thenReturn(doctors);
        assertEquals(doctors, doctorService.getAllDoctorsOrderBy(OrderBy.NAME, 0, 99));
        assertEquals(doctors.size(), doctorService.getNumberOfRecords());
    }

    @Test
    void testGetAllCategories() {
        var categories = List.of(category);
        when(mockCategoryDao.findAllCategories(mockDaoFactory.open())).thenReturn(categories);
        assertEquals(categories, doctorService.getAllCategories());
    }

    @Test
    void testAddDoctorWithNoExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            when(mockUserDao.insertUser(connection, user)).thenReturn(user.getId());
            when(mockDoctorDao.insertDoctor(connection, doctor)).thenReturn(doctor.getId());
            doNothing().when(mockUserDao).checkUniqueFields(connection, user);
            assertDoesNotThrow(()-> doctorService.addDoctor(doctor));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testAddDoctorWithExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doThrow(InputErrorsMessagesException.class).when(mockUserDao).checkUniqueFields(connection, user);
            when(mockUserDao.insertUser(connection, user)).thenReturn(user.getId());
            when(mockDoctorDao.insertDoctor(connection, doctor)).thenReturn(doctor.getId());
            assertThrows(InputErrorsMessagesException.class, () -> doctorService.addDoctor(doctor));
            doThrow(UnknownSqlException.class).when(mockUserDao).checkUniqueFields(connection, user);
            assertThrows(UnknownSqlException.class, () -> doctorService.addDoctor(doctor));
        } finally {
            mockDaoFactory.rollback(mockConn);
        }
    }

    @Test
    void testGetDoctorByUser() {
        when(mockDoctorDao.findDoctorByUserId(mockDaoFactory.open(), user.getId())).thenReturn(doctor);
        assertEquals(doctor, doctorService.getDoctorByUser(user));
    }

    @Test
    void testInsertCategoryWithNoExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            when(mockCategoryDao.insertCategory(connection, category)).thenReturn(category.getId());
            assertDoesNotThrow(() -> doctorService.addCategory(category));
        }
    }

    @Test
    void testInsertCategoryWithExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doThrow(InputErrorsMessagesException.class).when(mockCategoryDao).insertCategory(connection, category);
            assertThrows(InputErrorsMessagesException.class,() -> doctorService.addCategory(category));
            doThrow(UnknownSqlException.class).when(mockCategoryDao).insertCategory(connection, category);
            assertThrows(UnknownSqlException.class,() -> doctorService.addCategory(category));
        }
    }
}