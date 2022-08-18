package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Gender;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlPatientDao extends GenericDao<Patient> implements PatientDao {

    public MySqlPatientDao(DataSource ds) {
        super(ds);
    }

    private static final String INSERT_TEMPLATE = "INSERT INTO hospital.patient\n" +
            "(status, doctor_id, firstname, lastname, date_of_birth, gender, email)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    protected Patient mapToEntity(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getLong("id"));
        patient.setStatus(rs.getString("status"));
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
    public void checkUniqueEmail(Patient patient) {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT count(id) FROM patient WHERE email = ? AND id != ?")) {
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
    protected void mapFromEntity(PreparedStatement ps, Patient patient) throws SQLException {
        int k = 1;
        ps.setString(k++, patient.getStatus());
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
    public Patient findPatientById(long id) {
        return findEntity(getConnection(), "SELECT * FROM patient WHERE id =" + id);
    }

    public List<Patient> findAllPatients() {
        return findAll(getConnection(), "SELECT * FROM patient");
    }

    @Override
    public List<Patient> findPatientsWithoutDoctor() {
        return findEntities(getConnection(), "SELECT * FROM patient WHERE doctor_id != NULL");
    }

    @Override
    public List<Patient> findPatientsWithDoctor() {
        return findEntities(getConnection(), "SELECT * FROM patient WHERE doctor_id = NULL");
    }

    @Override
    public List<Patient> findPatientsOrderBy(OrderBy order) {
        String query = "SELECT * FROM patient";
        return findAll(getConnection(), query + " ORDER BY " + order.getField());
    }

    public void insertPatient(Patient patient) {
        insertEntity(getConnection(), INSERT_TEMPLATE, patient);
    }

}
