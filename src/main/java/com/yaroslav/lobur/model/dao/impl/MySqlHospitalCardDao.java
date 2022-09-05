package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.HospitalCardDao;
import com.yaroslav.lobur.model.entity.*;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.sql.*;
import java.util.List;

public class MySqlHospitalCardDao extends GenericDao<HospitalCard> implements HospitalCardDao {

    @Override
    protected HospitalCard mapToEntity(ResultSet rs) throws SQLException {
        HospitalCard hospitalCard = new HospitalCard();
        hospitalCard.setId(rs.getLong("id"));
        String diagnosis = rs.getString("diagnosis");
        if (diagnosis != null)
            hospitalCard.setDiagnosis(diagnosis);
        long patientId = rs.getLong("patient_id");
        if (patientId > 0) {
            Patient patient = new Patient();
            patient.setId(patientId);
            patient.setStatus(PatientStatus.valueOf(rs.getString("status")));
            patient.setFirstname(rs.getString("p.firstname"));
            patient.setLastname(rs.getString("p.lastname"));
            patient.setDateOfBirth(rs.getDate("p.date_of_birth"));
            patient.setGender(rs.getString("p.gender"));
            patient.setEmail(rs.getString("p.email"));
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
                hospitalCard.setPatient(patient);
            }
        }
        return hospitalCard;
    }

    @Override
    public int getNumberOfRecords() {
        return super.getNumberOfRecords();
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, HospitalCard hospitalCard) throws SQLException {
        int k = 1;
        if (hospitalCard.getDiagnosis() == null) {
            ps.setNull(k++, Types.VARCHAR);
        } else {
            ps.setString(k++, hospitalCard.getDiagnosis());
        }
        ps.setLong(k, hospitalCard.getPatient().getId());
    }

    public long insertHospitalCard(Connection connection, HospitalCard hospitalCard) {
        return insertEntity(connection, "INSERT INTO hospital_card(diagnosis, patient_id) VALUES (?, ?)", hospitalCard);
    }

    public void updateHospitalCard(Connection connection, HospitalCard hospitalCard) {
        updateEntity(connection, "UPDATE hospital_card SET  diagnosis = ?, patient_id = ? WHERE id =" + hospitalCard.getId(), hospitalCard);
    }

    public List<HospitalCard> findAllHospitalCards(Connection connection) {
        return findAll(connection,
            "SELECT SQL_CALC_FOUND_ROWS *\n" +
                "FROM hospital_card h\n" +
                "         JOIN patient p on h.patient_id = p.id\n" +
                "         JOIN doctor d on p.doctor_id = d.id\n" +
                "         JOIN user u on d.user_id = u.id\n" +
                "         JOIN category c on d.category_id = c.id");
    }

    @Override
    public List<HospitalCard> findAllHospitalCardsForDoctor(Connection connection, long id, int offset, int noOfRecords) {
        return findAll(connection, String.format(
                "SELECT SQL_CALC_FOUND_ROWS *\n" +
                        "FROM hospital_card h\n" +
                        "         JOIN patient p on h.patient_id = p.id\n" +
                        "         JOIN doctor d on p.doctor_id = d.id\n" +
                        "         JOIN user u on d.user_id = u.id\n" +
                        "         JOIN category c on d.category_id = c.id\n" +
                        "WHERE %d IN (SELECT doctor_id FROM patient WHERE id=patient_id)\n" +
                        "ORDER BY (SELECT lastname FROM patient WHERE patient_id=patient.id)\n" +
                        "DESC\n" +
                        "    LIMIT %d, %d",
                id, offset, noOfRecords));
    }

    public HospitalCard findHospitalCardById(Connection connection, long id) {
        return findEntity(connection,
                "SELECT SQL_CALC_FOUND_ROWS *\n" +
                "FROM hospital_card h\n" +
                "         JOIN patient p on h.patient_id = p.id\n" +
                "         JOIN doctor d on p.doctor_id = d.id\n" +
                "         JOIN user u on d.user_id = u.id\n" +
                "         JOIN category c on d.category_id = c.id\n" +
                "WHERE h.id = ?",
                id);
    }
}
