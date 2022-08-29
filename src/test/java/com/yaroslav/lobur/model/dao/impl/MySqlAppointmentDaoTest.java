package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.model.dao.AppointmentDao;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySqlAppointmentDaoTest {

    static DaoFactory daoFactory;
    static AppointmentDao appointmentDao;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        DaoFactory.init(MySqlDatasource.getDataSource());
        daoFactory = DaoFactory.getDaoFactory();
        appointmentDao = daoFactory.getAppointmentDao();
        MySqlDatasource.resetDatabase();
    }

    @Test
    @Order(1)
    void insertAppointment() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Appointment appointment = appointmentDao.findAppointmentById(con, 1);
            appointment.setStatus(AppointmentStatus.ONGOING);
            appointment.setType(AppointmentType.MEDICATION);
            long id = appointmentDao.insertAppointment(con,appointment);
            assertEquals(id, appointmentDao.findAppointmentById(con, id).getId());
        }
    }

    @Test
    @Order(2)
    void updateAppointment() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Appointment appointment = appointmentDao.findAppointmentById(con, 4);
            appointment.setStatus(AppointmentStatus.DONE);
            appointmentDao.updateAppointment(con, appointment);
            assertEquals(appointment.getStatus(), appointmentDao.findAppointmentById(con, 4).getStatus());
        }
    }

    @Test
    void findAppointmentByHospitalCardId() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> appointmentDao.findAppointmentByHospitalCardId(con, 555));
        }
    }

    @Test
    void findAppointmentById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> appointmentDao.findAppointmentById(con, 555));
        }
    }

    @Test
    @Order(3)
    void findAppointmentsByType() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Appointment> appointments = appointmentDao.findAppointmentsByType(con, Arrays.asList(AppointmentType.MEDICATION.name(), null, null), 0, 5);
            assertEquals(2, appointments.size());
        }
    }
}