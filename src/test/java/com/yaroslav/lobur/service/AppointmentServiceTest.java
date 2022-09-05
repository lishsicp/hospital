package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.AppointmentDao;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import com.yaroslav.lobur.utils.PasswordEncryptor;
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

class AppointmentServiceTest {

    @Mock
    DaoFactory mockDaoFactory;
    @Mock
    Connection mockConn;
    @Mock
    AppointmentDao mockAppointmentDao;
    @InjectMocks
    AppointmentService appointmentService;

    Appointment appointment;
    HospitalCard hospitalCard;
    @BeforeEach
    void setUp() throws Exception {
        try(var ignored = MockitoAnnotations.openMocks(this)) {
            when(mockDaoFactory.open()).thenReturn(mockConn);
            when(mockDaoFactory.beginTransaction()).thenReturn(mockConn);
            when(mockAppointmentDao.getNumberOfRecords()).thenReturn(1);
            doNothing().when(mockDaoFactory).commit(mockConn);
            doNothing().when(mockDaoFactory).rollback(mockConn);
            doNothing().when(mockDaoFactory).close(mockConn);
            appointment = new Appointment();
            appointment.setId(1);
            appointment.setTitle("test");
            appointment.setType(AppointmentType.MEDICATION);
            appointment.setStatus(AppointmentStatus.ONGOING);
            User user = new User();
            user.setId(1);
            appointment.setUser(user);
            hospitalCard = new HospitalCard();
            hospitalCard.setId(1);
            appointment.setHospitalCard(hospitalCard);
        }
    }

    @Test
    void testUpdateAppointmentWithNoExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doNothing().when(mockAppointmentDao).updateAppointment(connection, appointment);
            assertDoesNotThrow(() -> appointmentService.updateAppointment(appointment));
            mockDaoFactory.commit(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testUpdateAppointmentWithExceptions() throws SQLException {
        try (Connection connection = mockDaoFactory.beginTransaction()) {
            doThrow(InputErrorsMessagesException.class).when(mockAppointmentDao).updateAppointment(connection, appointment);
            assertThrows(InputErrorsMessagesException.class, () -> appointmentService.updateAppointment(appointment));
            doThrow(UnknownSqlException.class).when(mockAppointmentDao).updateAppointment(connection, appointment);
            assertThrows(UnknownSqlException.class, () -> appointmentService.updateAppointment(appointment));
            mockDaoFactory.rollback(connection);
            mockDaoFactory.endTransaction(connection);
        }
    }

    @Test
    void testCreateAppointmentWithNoExceptions() {
        when(mockAppointmentDao.insertAppointment(mockDaoFactory.beginTransaction(), appointment)).thenReturn(appointment.getId());
        assertEquals(appointment.getId(), appointmentService.createAppointment(appointment));
    }

    @Test
    void testCreateAppointmentWithExceptions() {
        when(mockAppointmentDao.insertAppointment(mockDaoFactory.beginTransaction(), appointment)).thenThrow(InputErrorsMessagesException.class);
        assertThrows(InputErrorsMessagesException.class,() -> appointmentService.createAppointment(appointment));
        when(mockDaoFactory.beginTransaction()).thenThrow(UnknownSqlException.class);
        assertThrows(UnknownSqlException.class,() -> appointmentService.createAppointment(appointment));
    }

    @Test
    void testGetAppointmentByHospitalCardId() {
        when(mockAppointmentDao.findAppointmentsByHospitalCardId(mockDaoFactory.open(), hospitalCard.getId())).thenReturn(List.of(appointment));
        assertEquals(appointmentService.getNumberOfRecords(), appointmentService.getAppointmentByHospitalCardId(hospitalCard.getId()).size());
        assertEquals(appointment, appointmentService.getAppointmentByHospitalCardId(hospitalCard.getId()).get(0));
        doNothing().when(mockDaoFactory).close(mockConn);
    }

    @Test
    void testGetAppointmentById() {
        when(mockAppointmentDao.findAppointmentById(mockDaoFactory.open(), appointment.getId())).thenReturn(appointment);
        assertEquals(appointment, appointmentService.getAppointmentById(appointment.getId()));
        doNothing().when(mockDaoFactory).close(mockConn);
    }

    @Test
    void testGetAppointmentsFiltered() {
        when(mockAppointmentDao.findAppointmentsByType(mockDaoFactory.open(), List.of(), 0, 99)).thenReturn(List.of(appointment));
        assertEquals(appointmentService.getNumberOfRecords(), appointmentService.getAppointmentsFiltered(List.of(), 0, 99).size());
        assertEquals(appointment, appointmentService.getAppointmentsFiltered(List.of(), 0, 99).get(0));
        doNothing().when(mockDaoFactory).close(mockConn);
    }
}