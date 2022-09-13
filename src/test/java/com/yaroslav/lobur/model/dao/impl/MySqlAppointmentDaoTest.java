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

    DaoFactory daoFactory = new MySqlDaoFactory(MySqlDatasource.getDataSource());
    AppointmentDao appointmentDao = daoFactory.getAppointmentDao();

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        MySqlDatasource.resetDatabase();
    }

    @Test
    @Order(1)
    void testInsertAppointment() throws SQLException {
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
    void testUpdateAppointment() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Appointment appointment = appointmentDao.findAppointmentById(con, 4);
            appointment.setStatus(AppointmentStatus.DONE);
            appointmentDao.updateAppointment(con, appointment);
            assertEquals(appointment.getStatus(), appointmentDao.findAppointmentById(con, 4).getStatus());
        }
    }

    @Test
    void testFindAppointmentByHospitalCardId() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertEquals(0, appointmentDao.findAppointmentsByHospitalCardId(con, 555).size());
        }
    }

    @Test
    void testFindAppointmentById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertThrows(EntityNotFoundException.class, () -> appointmentDao.findAppointmentById(con, 555));
        }
    }

    @Test
    @Order(3)
    void testFindAppointmentsByType() throws SQLException {
        try (Connection con = daoFactory.open()) {
            List<Appointment> appointments = appointmentDao.findAppointmentsByType(con, Arrays.asList(AppointmentType.MEDICATION.name(), null, null), 0, 5);
            assertEquals(2, appointments.size());
            assertEquals(appointmentDao.getNumberOfRecords(), appointments.size());
        }
    }
}