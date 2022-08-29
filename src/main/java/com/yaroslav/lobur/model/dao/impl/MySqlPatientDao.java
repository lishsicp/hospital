package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlPatientDao extends GenericDao<Patient> implements PatientDao {

    private static MySqlPatientDao instance;

    public static PatientDao getInstance() {
        if (instance == null) {
            instance = new MySqlPatientDao();
        }
        return instance;
    }

    private MySqlPatientDao(){}


private static final String INSERT_TEMPLATE = "INSERT INTO patient\n" +
            "(status, doctor_id, firstname, lastname, date_of_birth, gender, email)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_TEMPLATE = "UPDATE patient SET  status = ?, doctor_id = ?, firstname = ?, lastname = ?, date_of_birth = ?, gender = ?, email = ? WHERE id =";

    @Override
    protected Patient mapToEntity(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getLong("id"));
        patient.setStatus(PatientStatus.valueOf(rs.getString("status")));
        long doctorId = rs.getLong("doctor_id");
        if (doctorId > 0) {
            Doctor doctor = new Doctor();
            doctor.setId(doctorId);
            patient.setDoctor(doctor);
        }
        patient.setFirstname(rs.getString("firstname"));
        patient.setLastname(rs.getString("lastname"));
        patient.setDateOfBirth(rs.getDate("date_of_birth"));
        patient.setGender(rs.getString("gender"));
        patient.setEmail(rs.getString("email"));
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
        return findEntity(connection, "SELECT * FROM patient WHERE id = ?", id);
    }

    public List<Patient> findAllPatients(Connection connection) {
        return findAll(connection, "SELECT * FROM patient");
    }

    @Override
    public List<Patient> findPatientsWithoutDoctor(Connection connection, OrderBy order, int offset, int noOfRecords) {
        return findAll(connection, String.format("SELECT SQL_CALC_FOUND_ROWS * FROM patient WHERE doctor_id IS NULL ORDER BY %s LIMIT %d, %d", order.getField(), offset, noOfRecords));
    }

    @Override
    public List<Patient> findPatientsOrderBy(Connection connection, OrderBy order, int offset, int noOfRecords) {
        return findAll(connection, String.format("SELECT SQL_CALC_FOUND_ROWS * FROM patient ORDER BY %s LIMIT %d, %d", order.getField(), offset, noOfRecords));
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
