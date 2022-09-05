package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Appointment;

import java.sql.Connection;
import java.util.List;

public interface AppointmentDao {
    long insertAppointment(Connection connection, Appointment appointment);
    void updateAppointment(Connection connection, Appointment appointment);
    List<Appointment> findAppointmentsByHospitalCardId(Connection connection, long id);

    Appointment findAppointmentById(Connection connection, long id);

    List<Appointment> findAppointmentsByType(Connection connection, List<String> types, int offset, int noOfRecords);

    int getNumberOfRecords();
}
