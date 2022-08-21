package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.util.List;

public interface PatientDao {
    Patient findPatientById(Connection connection, long id);
    List<Patient> findPatientsWithoutDoctor(Connection connection);
    List<Patient> findPatientsWithDoctor(Connection connection);
    List<Patient> findPatientsOrderBy(Connection connection, OrderBy order);

    List<Patient> findAllPatients(Connection connection);
    void insertPatient(Connection connection, Patient patient);

    void deletePatient(Connection connection, long id);

    void updatePatient(Connection connection, Patient patient);
    void checkUniqueEmail(Connection connection, Patient patient);
}
