package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.AppointmentDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.Appointment;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlAppointmentDao extends GenericDao<Appointment> implements AppointmentDao {

    protected MySqlAppointmentDao(DataSource ds) {
        super(ds);
    }

    @Override
    protected Appointment mapToEntity(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void mapFromEntity(PreparedStatement statement, Appointment entity) throws SQLException {

    }
}
