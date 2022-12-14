package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.exceptions.UnknownSqlException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DaoFactory {

    protected DataSource ds;

    protected DaoFactory(DataSource dataSource) {
        this.ds = dataSource;
    }

    public abstract UserDao getUserDao();
    public abstract PatientDao getPatientDao();
    public abstract DoctorDao getDoctorDao();
    public abstract HospitalCardDao getHospitalCardDao();
    public abstract AppointmentDao getAppointmentDao();
    public abstract CategoryDao getCategoryDao();

    public abstract Connection open();

    public Connection beginTransaction(){
        Connection connection = open();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        }
        return connection;
    }

    public void commit(Connection connection) {
        if (connection != null)
            try {
                connection.commit();
            } catch (SQLException e) {
                throw new UnknownSqlException(e.getMessage());
            }
    }

    public void rollback(Connection connection) {
        if (connection != null)
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new UnknownSqlException(e.getMessage());
            }
    }

    public void endTransaction(Connection connection) {
        if (connection != null)
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new UnknownSqlException(e.getMessage());
            }
    }

    public void close(Connection connection) {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                throw new UnknownSqlException(e.getMessage());
            }
    }

}
