package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlPatientDao extends GenericDao<Patient> implements PatientDao {

private static final String INSERT_TEMPLATE = "INSERT INTO patient\n" +
            "(status, doctor_id, firstname, lastname, date_of_birth, gender, email)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_TEMPLATE = "UPDATE patient SET  status = ?, doctor_id = ?, firstname = ?, lastname = ?, date_of_birth = ?, gender = ?, email = ? WHERE id =";

    @Override
    protected Patient mapToEntity(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getLong("p.id"));
        patient.setStatus(PatientStatus.valueOf(rs.getString("status")));
        long doctorId = rs.getLong("doctor_id");
        if (doctorId > 0) {
            Doctor doctor = new Doctor();
            User user = new User();
            Category category = new Category();
            doctor.setId(doctorId);
            category.setId(rs.getLong("c.id"));
            category.setName(rs.getString("category"));
            user.setId(rs.getLong("u.id"));
            user.setLogin("");
            user.setPassword("");
            user.setFirstname(rs.getString("u.firstname"));
            user.setLastname(rs.getString("u.lastname"));
            user.setDateOfBirth(rs.getDate("u.date_of_birth"));
            user.setGender(rs.getString("u.gender"));
            user.setEmail(rs.getString("u.email"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            user.setLocale(Locale.UK);
            user.setRole(Role.getById(rs.getInt("role_id")));
            doctor.setUser(user);
            doctor.setCategory(category);
            patient.setDoctor(doctor);
        }
        patient.setFirstname(rs.getString("p.firstname"));
        patient.setLastname(rs.getString("p.lastname"));
        patient.setDateOfBirth(rs.getDate("p.date_of_birth"));
        patient.setGender(rs.getString("p.gender"));
        patient.setEmail(rs.getString("p.email"));
        return patient;
    }

    @Override
    public void checkUniqueEmail(Connection connection, Patient patient) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT count(id) FROM patient WHERE email = ? AND id != ?")) {
            ps.setString(1, patient.getEmail());
            ps.setLong(2, patient.getId());
            ResultSet rs = ps.executeQuery();
            Map<String, String> errors = new HashMap<>();
            rs.next();
            if (rs.getInt(1) > 0) {
                errors.put("email", "validation.user.email.exist");
            }
            if (!errors.isEmpty())
                throw new InputErrorsMessagesException(errors);
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public int getNumberOfRecords() {
        return super.getNumberOfRecords();
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, Patient patient) throws SQLException {
        int k = 1;
        ps.setString(k++, patient.getStatus().name());
        if (patient.getDoctor() == null) {
            ps.setNull(k++, Types.INTEGER);
        } else {
            ps.setLong(k++, patient.getDoctor().getId());
        }
        ps.setString(k++, patient.getFirstname());
        ps.setString(k++, patient.getLastname());
        ps.setDate(k++, java.sql.Date.valueOf(patient.getDateOfBirth().toString()));
        ps.setString(k++, patient.getGender());
        ps.setString(k, patient.getEmail());
    }

    @Override
    public Patient findPatientById(Connection connection, long id) {
        return findEntity(connection, "SELECT * FROM patient p\n" +
                "    LEFT JOIN doctor d on p.doctor_id = d.id\n" +
                "    LEFT JOIN user u on d.user_id = u.id\n" +
                "    LEFT JOIN category c on d.category_id = c.id" +
                "         WHERE p.id = ?", id);
    }

    public List<Patient> findAllPatients(Connection connection) {
        return findAll(connection, "SELECT * FROM patient p\n" +
                "    LEFT JOIN doctor d on p.doctor_id = d.id\n" +
                "    LEFT JOIN user u on d.user_id = u.id\n" +
                "    LEFT JOIN category c on d.category_id = c.id");
    }

    @Override
    public List<Patient> findPatientsWithoutDoctor(Connection connection, OrderBy order, int offset, int noOfRecords) {
        return findAll(connection, String.format("SELECT SQL_CALC_FOUND_ROWS * FROM patient p\n" +
                "    LEFT JOIN doctor d on p.doctor_id = d.id\n" +
                "    LEFT JOIN user u on d.user_id = u.id\n" +
                "    LEFT JOIN category c on d.category_id = c.id \n" +
                "                             WHERE doctor_id IS NULL \n" +
                "                             ORDER BY p.%s \n" +
                "                             LIMIT %d, %d", order.getField(), offset, noOfRecords));
    }

    @Override
    public List<Patient> findPatientsOrderBy(Connection connection, OrderBy order, int offset, int noOfRecords) {
        return findAll(connection, String.format("SELECT SQL_CALC_FOUND_ROWS * FROM patient p\n" +
                "    LEFT JOIN doctor d on p.doctor_id = d.id\n" +
                "    LEFT JOIN user u on d.user_id = u.id\n" +
                "    LEFT JOIN category c on d.category_id = c.id \n" +
                "                             ORDER BY p.%s \n" +
                "                             LIMIT %d, %d", order.getField(), offset, noOfRecords));
    }

    public long insertPatient(Connection connection, Patient patient) {
        return insertEntity(connection, INSERT_TEMPLATE, patient);
    }

    public void deletePatient(Connection connection, long id) {
        deleteEntity(connection, "DELETE FROM patient WHERE id=" + id);
    }

    @Override
    public void updatePatient(Connection connection, Patient patient) {
        updateEntity(connection, UPDATE_TEMPLATE + patient.getId(), patient);
    }

}
