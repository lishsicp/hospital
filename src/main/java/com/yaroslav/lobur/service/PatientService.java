package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    private static final DaoFactory daoFactory;
    private static final PatientDao patientDao;

    static {
        daoFactory = DaoFactory.getDaoFactory();
        patientDao = daoFactory.getPatientDao();
    }

    public List<Patient> getAllPatients() {
        return patientDao.findAllPatients(daoFactory.open());
    }

    public List<Patient> getAllPatientsSorted(OrderBy order) {
        Connection con = null;
        List<Patient> patients = null;
        try {
            con = daoFactory.open();
            patients =  patientDao.findPatientsOrderBy(con, order);
        } catch (UnknownSqlException e) {
            logger.error("", e);
        } finally {
            daoFactory.close(con);
        }
        return patients;
    }

    public void addPatient(Patient patient) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            patientDao.checkUniqueEmail(con, patient);
            patientDao.insertPatient(con, patient);
            daoFactory.commit(con);
        } catch (InputErrorsMessagesException e) {
            logger.debug("Email exist: {}", patient.getEmail());
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.endTransaction(con);
        }
    }

    public void deletePatient(long id) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            patientDao.deletePatient(con, id);
            daoFactory.commit(con);
        } catch (DBExceptionMessages e) {
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.endTransaction(con);
        }
    }

    public void updatePatient(Patient patient) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            Patient p = patientDao.findPatientById(con, patient.getId());
            if (!p.getEmail().equals(patient.getEmail())) {
                patientDao.checkUniqueEmail(con, patient);
            }
            patientDao.updatePatient(con, patient);
            daoFactory.commit(con);
        } catch (InputErrorsMessagesException e) {
            logger.debug("Email exist: {}", patient.getEmail());
            daoFactory.rollback(con);
            throw e;
        } catch (DBExceptionMessages | UnknownSqlException e) {
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.endTransaction(con);
        }
    }
}
