package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.DoctorDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlDoctorDao extends GenericDao<Doctor> implements DoctorDao {

    private final String SELECT_NUMBER_OF_PATIENTS = "(SELECT count(id) FROM patient WHERE patient.doctor_id = doctor.id) as NumberOfPatients ";

    public MySqlDoctorDao(DataSource ds) {
        super(ds);
    }

    @Override
    protected Doctor mapToEntity(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getLong("id"));
//        Category category = new MySqlCategoryDao(ds).findCategoryById(rs.getLong("category_id"));
//        doctor.setCategory(category);
//        User user = new MySqlUserDao(ds).findUserById(rs.getLong("user_id"));
//        doctor.setUser(user);
//        doctor.setNumberOfPatients(rs.getInt("numberOfPatients"));
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
        ps.setLong(1, doctor.getId());
        ps.setString(2, doctor.getCategory().getName());
        ps.setLong(3, doctor.getUser().getId());
    }

    public List<Doctor> findAllDoctors() {
        return findAll(getConnection(), "SELECT *" + ", " + SELECT_NUMBER_OF_PATIENTS + "FROM doctor");
    }

    @Override
    public List<Doctor> findDoctorsOrderBy(OrderBy order) {
        String query = "SELECT d.id, u.lastname, d.user_id, d.category_id, c.category," +
                "(SELECT count(id) FROM patient WHERE patient.doctor_id = d.id) as NumberOfPatients" +
                " FROM doctor d JOIN category c ON d.category_id = c.id JOIN user u ON u.id = d.user_id";
        return findAll(getConnection(), query + " ORDER BY " + order.getField());
    }
}

