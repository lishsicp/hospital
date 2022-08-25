package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.util.List;

public interface DoctorDao {
    List<Doctor> findAllDoctors(Connection con);
    List<Doctor> findDoctorsOrderBy(Connection con, OrderBy order, int offset, int noOfRecords);

    Doctor findDoctorById(Connection con, long id);

    Doctor findDoctorByUserId(Connection con, long id);

    int getNumberOfRecords();

    List<Doctor> findDoctorsByCategory(Connection connection, long categoryId);

    long insertDoctor(Connection con, Doctor doctor);
}
