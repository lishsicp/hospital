package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);
    private final PatientDao patientDao;

    public PatientService(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public List<Patient> getAllPatients() {
        return patientDao.findAllPatients();
    }

    public List<Patient> getAllPatientsSorted(OrderBy order) {
        return patientDao.findPatientsOrderBy(order);
    }

    public void addPatient(Patient patient) {
        try {
            patientDao.checkUniqueEmail(patient);
            patientDao.insertPatient(patient);
        } catch (InputErrorsMessagesException e) {
            logger.debug("Email exist: {}", patient.getEmail());
            throw e;
        }
    }
}
