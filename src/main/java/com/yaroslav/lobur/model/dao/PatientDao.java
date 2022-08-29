package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.util.List;

public interface PatientDao {
    Patient findPatientById(Connection connection, long id);
    List<Patient> findPatientsWithoutDoctor(Connection connection, OrderBy order, int offset, int noOfRecords);
    List<Patient> findPatientsOrderBy(Connection connection, OrderBy order, int offset, int noOfRecords);

    List<Patient> findAllPatients(Connection connection);
    long insertPatient(Connection connection, Patient patient);

    void deletePatient(Connection connection, long id);

    void updatePatient(Connection connection, Patient patient);
    void checkUniqueEmail(Connection connection, Patient patient);

    int getNumberOfRecords();
}
