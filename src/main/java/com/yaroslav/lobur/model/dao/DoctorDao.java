package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.util.List;

public interface DoctorDao {
    List<Doctor> findAllDoctors();
    List<Doctor> findDoctorsOrderBy(OrderBy order);
}
