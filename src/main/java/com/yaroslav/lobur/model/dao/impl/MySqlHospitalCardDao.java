package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.HospitalCardDao;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;

import java.sql.*;
import java.util.List;

public class MySqlHospitalCardDao extends GenericDao<HospitalCard> implements HospitalCardDao {

    private static MySqlHospitalCardDao instance;

    public static HospitalCardDao getInstance() {
        if (instance == null) {
            instance = new MySqlHospitalCardDao();
        }
        return instance;
    }

    private MySqlHospitalCardDao(){}

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
            hospitalCard.setPatient(patient);
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
        return findAll(connection,"SELECT * FROM hospital_card");
    }

    @Override
    public List<HospitalCard> findAllHospitalCardsForDoctor(Connection connection, long id, int offset, int noOfRecords) {
        return findAll(connection, String.format("SELECT SQL_CALC_FOUND_ROWS h.id, h.diagnosis, h.patient_id\n" +
                "FROM hospital_card h WHERE %d IN (SELECT doctor_id FROM patient WHERE id=patient_id)\n" +
                "ORDER BY (SELECT lastname FROM patient WHERE patient_id=patient.id) DESC\n" +
                "LIMIT %d, %d", id, offset, noOfRecords));
    }

    public HospitalCard findHospitalCardById(Connection connection, long id) {
        return findEntity(connection, "SELECT * FROM hospital_card WHERE id = ?", id);
    }
}
