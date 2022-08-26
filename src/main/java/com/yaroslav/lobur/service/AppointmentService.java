package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private static final UserDao userDao;
    private static final PatientDao patientDao;
    private static final HospitalCardDao hospitalCardDao;
    private static final AppointmentDao appointmentDao;

    private static final DaoFactory daoFactory;

    static {
        daoFactory = DaoFactory.getDaoFactory();
        userDao = daoFactory.getUserDao();
        patientDao = daoFactory.getPatientDao();
        hospitalCardDao = daoFactory.getHospitalCardDao();
        appointmentDao = daoFactory.getAppointmentDao();
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

    public Appointment getAppointmentByHospitalCard(HospitalCard hospitalCard) {
        Connection con = null;
        Appointment appointment;
        try {
            con = daoFactory.open();
            appointment = appointmentDao.findAppointmentByHospitalCardId(con, hospitalCard.getId());
            appointment.setHospitalCard(hospitalCard);
        } finally {
            daoFactory.close(con);
        }
        return appointment;
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
            List<HospitalCard> hospitalCards = hospitalCardDao.findAllHospitalCards(con);
            List<Patient> patients = patientDao.findAllPatients(con);
            List<User> users = userDao.findAllUsers(con);
            appointments.forEach(a -> {
                a.setHospitalCard(hospitalCards
                                .stream()
                                .filter(h -> h.getId() == a.getHospitalCard().getId())
                                .findFirst().orElse(null));
                a.getHospitalCard().setPatient(patients
                        .stream()
                        .filter(p -> p.getId() == a.getHospitalCard().getPatient().getId())
                        .findFirst()
                        .orElse(null));
                if (a.getUser() != null) {
                    a.setUser(users.stream()
                            .filter(u -> u.getId() == a.getUser().getId())
                            .findFirst()
                            .orElse(null));
                }
            });
        } finally {
            daoFactory.close(con);
        }
        return appointments;
    }

    public int getNumberOfRecords() {
        return appointmentDao.getNumberOfRecords();
    }
}
