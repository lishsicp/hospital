package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDaoFactory extends DaoFactory {

    public MySqlDaoFactory(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public UserDao getUserDao() {
        return new MySqlUserDao();
    }

    @Override
    public PatientDao getPatientDao() {
        return new MySqlPatientDao();
    }

    @Override
    public DoctorDao getDoctorDao() {
        return new MySqlDoctorDao();
    }

    @Override
    public HospitalCardDao getHospitalCardDao() {
        return new MySqlHospitalCardDao();
    }

    @Override
    public AppointmentDao getAppointmentDao() {
        return new MySqlAppointmentDao();
    }

    @Override
    public CategoryDao getCategoryDao() {
        return new MySqlCategoryDao();
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
