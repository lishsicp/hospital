package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import db.MySqlDatasource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    static private AppointmentService appointmentService;

    @BeforeAll
    static void setUp() {
        DaoFactory.init(MySqlDatasource.getDataSource());
        appointmentService = new AppointmentService();
    }

    @Test
    void updateAppointment() {
        Appointment appointment = appointmentService.getAppointmentById(1);
        appointment.setTitle("test");
        appointmentService.updateAppointment(appointment);
        assertEquals(appointment.getTitle(), appointmentService.getAppointmentById(1).getTitle());
    }

    @Test
    void getAppointmentByHospitalCard() {
        HospitalCard hospitalCard = new HospitalCard();
        Patient patient = new Patient();
        patient.setId(1);
        hospitalCard.setId(1);
        hospitalCard.setPatient(patient);
        assertEquals(1, appointmentService.getAppointmentByHospitalCard(hospitalCard).getId());
    }

    @Test
    void getAppointmentById() {
        Appointment appointment = appointmentService.getAppointmentById(1);
        assertEquals(AppointmentType.OPERATION, appointment.getType());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getAppointmentById(999));
    }

    @Test
    void getAppointmentsFiltered() {
        List<Appointment> appointmentList = appointmentService.getAppointmentsFiltered(Arrays.asList(
                AppointmentType.PROCEDURE.name(),
                AppointmentType.OPERATION.name(),
                AppointmentType.MEDICATION.name()),
        0, 100);
        assertEquals(appointmentList.size(), appointmentService.getNumberOfRecords());

    }
}