package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.DoctorDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlDoctorDao extends GenericDao<Doctor> implements DoctorDao {

    private static final String SELECT_NUMBER_OF_PATIENTS = " (SELECT count(id) FROM patient WHERE patient.doctor_id = d.id) as NumberOfPatients ";

    private static MySqlDoctorDao instance;

    public static DoctorDao getInstance() {
        if (instance == null) {
            instance = new MySqlDoctorDao();
        }
        return instance;
    }

    private MySqlDoctorDao(){}


    @Override
    protected Doctor mapToEntity(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getLong("id"));
        Category c = new Category();
        c.setId(rs.getLong("category_id"));
        doctor.setCategory(c);
        User user = new User();
        user.setId(rs.getLong("user_id"));
        doctor.setUser(user);
        doctor.setNumberOfPatients(rs.getInt("numberOfPatients"));
        return doctor;
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, Doctor doctor) throws SQLException {
        ps.setLong(1, doctor.getCategory().getId());
        ps.setLong(2, doctor.getUser().getId());
    }

    public List<Doctor> findAllDoctors(Connection con) {
        return findAll(con, "SELECT *" + ", " + SELECT_NUMBER_OF_PATIENTS + "FROM doctor d");
    }

    @Override
    public List<Doctor> findDoctorsOrderBy(Connection con, OrderBy order, int offset, int noOfRecords) {
        String query = "SELECT SQL_CALC_FOUND_ROWS d.id, u.lastname, d.user_id, d.category_id, c.category," +
                SELECT_NUMBER_OF_PATIENTS +
                "FROM doctor d JOIN category c ON d.category_id = c.id JOIN user u ON u.id = d.user_id";
        return findAll(con, query + " ORDER BY " + order.getField() + " LIMIT " + offset + ", " + noOfRecords);
    }

    @Override
    public Doctor findDoctorById(Connection connection, long id) {
        return findEntity(connection, "SELECT *" + ", " + SELECT_NUMBER_OF_PATIENTS + "FROM doctor d WHERE id=?", id);
    }

    @Override
    public long insertDoctor(Connection con, Doctor doctor) {
        return insertEntity(con, "INSERT INTO hospital.doctor (category_id, user_id) VALUES (?, ?)", doctor);
    }
}

