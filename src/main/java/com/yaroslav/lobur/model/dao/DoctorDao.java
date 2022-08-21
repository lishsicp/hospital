package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.util.List;

public interface DoctorDao {
    List<Doctor> findAllDoctors(Connection con);
    List<Doctor> findDoctorsOrderBy(Connection con, OrderBy order, int offset, int noOfRecords);

    Doctor findDoctorById(Connection con, long id);

    int getNoOfRecords();

    long insertDoctor(Connection con, Doctor doctor);
}
