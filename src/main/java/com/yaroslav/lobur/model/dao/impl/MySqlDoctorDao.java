package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.DoctorDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlDoctorDao extends GenericDao<Doctor> implements DoctorDao {

    private static final String SELECT_NUMBER_OF_PATIENTS = " (SELECT count(id) FROM patient WHERE patient.doctor_id = d.id) as NumberOfPatients ";

    @Override
    protected Doctor mapToEntity(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getLong("id"));
        Category category = new Category();
        category.setId(rs.getLong("category_id"));
        category.setName(rs.getString("category"));
        doctor.setCategory(category);
        User user = new User();
        user.setId(rs.getLong("user_id"));
        doctor.setUser(user);
        user.setId(rs.getLong("u.id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setDateOfBirth(rs.getDate("date_of_birth"));
        user.setGender(rs.getString("gender"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setLocale(Locale.valueOf(rs.getString("locale")));
        user.setRole(Role.getById(rs.getInt("role_id")));
        doctor.setUser(user);
        doctor.setCategory(category);
        doctor.setNumberOfPatients(rs.getInt("numberOfPatients"));
        return doctor;
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, Doctor doctor) throws SQLException {
        ps.setLong(1, doctor.getCategory().getId());
        ps.setLong(2, doctor.getUser().getId());
    }

    public List<Doctor> findAllDoctors(Connection con) {
        return findAll(con,
                "SELECT SQL_CALC_FOUND_ROWS *" + ", " +
                SELECT_NUMBER_OF_PATIENTS +
                "FROM doctor d " +
                "         JOIN user u on d.user_id = u.id " +
                "         JOIN category c on d.category_id = c.id");
    }

    @Override
    public List<Doctor> findDoctorsOrderBy(Connection con, OrderBy order, int offset, int noOfRecords) {
        String query = "SELECT SQL_CALC_FOUND_ROWS *," +
                SELECT_NUMBER_OF_PATIENTS +
                "FROM doctor d " +
                "         JOIN user u on d.user_id = u.id " +
                "         JOIN category c on d.category_id = c.id ";
        return findAll(con, query + " ORDER BY " + order.getField() + " LIMIT " + offset + ", " + noOfRecords);
    }

    public List<Doctor> findDoctorsByCategory(Connection connection, long categoryId) {
        return findEntities(connection,
                "SELECT SQL_CALC_FOUND_ROWS *, " +
                    SELECT_NUMBER_OF_PATIENTS +
                        "FROM doctor d" +
                        "         JOIN user u on d.user_id = u.id " +
                        "         JOIN category c on d.category_id = c.id " +
                    "WHERE category_id = ? ORDER BY NumberOfPatients LIMIT 5",
                    categoryId);
    }

    @Override
    public Doctor findDoctorById(Connection connection, long id) {
        return findEntity(connection, "SELECT *" + ", " + SELECT_NUMBER_OF_PATIENTS +
                "FROM doctor d " +
                "         JOIN user u on d.user_id = u.id " +
                "         JOIN category c on d.category_id = c.id " +
                "WHERE d.id=?", id);
    }

    @Override
    public Doctor findDoctorByUserId(Connection connection, long id) {
        return findEntity(connection, "SELECT *" + ", " + SELECT_NUMBER_OF_PATIENTS +
                "FROM doctor d " +
                "         JOIN user u on d.user_id = u.id " +
                "         JOIN category c on d.category_id = c.id " +
                "WHERE d.user_id=?", id);
    }

    @Override
    public int getNumberOfRecords() {
        return super.getNumberOfRecords();
    }

    @Override
    public long insertDoctor(Connection con, Doctor doctor) {
        return insertEntity(con, "INSERT INTO doctor (category_id, user_id) VALUES (?, ?)", doctor);
    }
}

