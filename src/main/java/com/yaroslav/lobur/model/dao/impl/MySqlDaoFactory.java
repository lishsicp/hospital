package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDaoFactory extends DaoFactory {

    private static MySqlDaoFactory instance;

    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new MySqlDaoFactory();
        }
        return instance;
    }

    private MySqlDaoFactory(){}

    @Override
    public UserDao getUserDao() {
        return MySqlUserDao.getInstance();
    }

    @Override
    public PatientDao getPatientDao() {
        return MySqlPatientDao.getInstance();
    }

    @Override
    public DoctorDao getDoctorDao() {
        return MySqlDoctorDao.getInstance();
    }

    @Override
    public HospitalCardDao getHospitalCardDao() {
        return MySqlHospitalCardDao.getInstance();
    }

    @Override
    public AppointmentDao getAppointmentDao() {
        return MySqlAppointmentDao.getInstance();
    }

    @Override
    public CategoryDao getCategoryDao() {
        return MySqlCategoryDao.getInstance();
    }

    @Override
    public Connection open() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new UnknownSqlException("Error when getting a connection", e.getCause());
        }
    }
}
