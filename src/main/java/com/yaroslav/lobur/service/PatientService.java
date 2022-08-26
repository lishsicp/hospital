package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.HospitalCardDao;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.HospitalCard;
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
    private static final HospitalCardDao hospitalCardDao;

    static {
        daoFactory = DaoFactory.getDaoFactory();
        patientDao = daoFactory.getPatientDao();
        hospitalCardDao = daoFactory.getHospitalCardDao();
    }

    public Patient getPatientById(long id) {
        Connection con = null;
        Patient patient;
        try {
            con = daoFactory.open();
            patient = patientDao.findPatientById(con, id);
            if (patient.getDoctor() != null) {
                Doctor doctor = daoFactory.getDoctorDao().findDoctorById(con, patient.getDoctor().getId());
                patient.setDoctor(doctor);
            }
        } finally {
            daoFactory.close(con);
        }
        return patient;
    }

    public HospitalCard getHospitalCardById(long id) {
        Connection con = null;
        HospitalCard hospitalCard;
        try {
            con = daoFactory.open();
            hospitalCard = hospitalCardDao.findHospitalCardById(con, id);
        } finally {
            daoFactory.close(con);
        }
        return hospitalCard;
    }

    public List<Patient> getAllPatients() {
        Connection con = null;
        List<Patient> patients;
        try {
            con = daoFactory.open();
            patients = patientDao.findAllPatients(con);
        } finally {
            daoFactory.close(con);
        }
        return patients;
    }

    public List<HospitalCard> getAllHospitalCardSorted(long id, int offset, int noOfRecords) {
        Connection con = null;
        List<HospitalCard> hospitalCards;
        try {
            con = daoFactory.open();
            List<Patient> patients =  patientDao.findAllPatients(con);
            hospitalCards =  hospitalCardDao.findAllHospitalCardsForDoctor(con, id, offset, noOfRecords);
            hospitalCards.forEach(hospitalCard -> hospitalCard
                            .setPatient(patients
                                    .stream()
                                    .filter(patient -> patient.getId() == hospitalCard.getPatient().getId())
                                    .findFirst()
                                    .orElse(null)));

        } finally {
            daoFactory.close(con);
        }
        return hospitalCards;
    }

    public List<Patient> getAllPatientsSorted(OrderBy order, int offset, int noOfRecords) {
        Connection con = null;
        List<Patient> patients = null;
        try {
            con = daoFactory.open();
            patients =  patientDao.findPatientsOrderBy(con, order, offset, noOfRecords);
        } finally {
            daoFactory.close(con);
        }
        return patients;
    }

    public List<Patient> getPatientsWithoutDoctor(OrderBy order, int offset, int noOfRecords) {
        Connection con = null;
        List<Patient> patients = null;
        try {
            con = daoFactory.open();
            patients =  patientDao.findPatientsWithoutDoctor(con, order, offset, noOfRecords);
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
            long patientId = patientDao.insertPatient(con, patient);
            HospitalCard hc = new HospitalCard();
            patient.setId(patientId);
            hc.setPatient(patient);
            hospitalCardDao.insertHospitalCard(con, hc);
            daoFactory.commit(con);
        } catch (UnknownSqlException e) {
            daoFactory.rollback(con);
            throw e;
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
        } catch (DBExceptionMessages | UnknownSqlException e) {
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

    public void updateHospitalCard(HospitalCard hospitalCard) {
        Connection con = null;
        try {
            con = daoFactory.beginTransaction();
            hospitalCardDao.updateHospitalCard(con, hospitalCard);
            daoFactory.commit(con);
        } catch (DBExceptionMessages | UnknownSqlException e) {
            daoFactory.rollback(con);
            throw e;
        } finally {
            daoFactory.endTransaction(con);
        }
    }

    public int getNumberOfRecords() {
        return patientDao.getNumberOfRecords();
    }

    public int getNumberOfRecordsHC() {
        return hospitalCardDao.getNumberOfRecords();
    }


}
