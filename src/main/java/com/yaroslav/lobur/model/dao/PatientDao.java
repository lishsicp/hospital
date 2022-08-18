package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.util.List;

public interface PatientDao {
    Patient findPatientById(long id);
    List<Patient> findPatientsWithoutDoctor();
    List<Patient> findPatientsWithDoctor();
    List<Patient> findPatientsOrderBy(OrderBy order);

    List<Patient> findAllPatients();
    void insertPatient(Patient patient);

    void checkUniqueEmail(Patient patient);
}
