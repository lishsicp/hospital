package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;
import com.yaroslav.lobur.model.entity.Appointment;

import java.sql.Connection;
import java.util.List;

public class AppointmentService {

    private final DaoFactory daoFactory;

    private final AppointmentDao appointmentDao;

    public AppointmentService(DaoFactory daoFactory, AppointmentDao appointmentDao) {
        this.daoFactory = daoFactory;
        this.appointmentDao = appointmentDao;
    }

    public long createAppointment(Appointment appointment) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            long id = appointmentDao.insertAppointment(con, appointment);
            daoFactory.commit(con);
            return id;
        } catch (InputErrorsMessagesException | UnknownSqlException e) {
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.endTransaction(con);
        }
    }

    public void updateAppointment(Appointment appointment) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            appointmentDao.updateAppointment(con, appointment);
            daoFactory.commit(con);
        } catch (InputErrorsMessagesException | UnknownSqlException e) {
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.endTransaction(con);
        }
    }

    public List<Appointment> getAppointmentByHospitalCardId(long hospitalCardId) {
        Connection con = null;
        List<Appointment> appointments;
        try {
            con = daoFactory.open();
            appointments = appointmentDao.findAppointmentsByHospitalCardId(con, hospitalCardId);
        } finally {
            daoFactory.close(con);
        }
        return appointments;
    }


    public Appointment getAppointmentById(long id) {
        Connection con = null;
        Appointment appointment;
        try {
            con = daoFactory.open();
            appointment = appointmentDao.findAppointmentById(con, id);
        } finally {
            daoFactory.close(con);
        }
        return appointment;
    }

    public List<Appointment> getAppointmentsFiltered(List<String> appointmentTypes, int offset, int noOfRecords) {
        Connection con = null;
        List<Appointment> appointments;
        try {
            con = daoFactory.open();
            appointments = appointmentDao.findAppointmentsByType(con, appointmentTypes, offset, noOfRecords);
        } finally {
            daoFactory.close(con);
        }
        return appointments;
    }

    public int getNumberOfRecords() {
        return appointmentDao.getNumberOfRecords();
    }
}
