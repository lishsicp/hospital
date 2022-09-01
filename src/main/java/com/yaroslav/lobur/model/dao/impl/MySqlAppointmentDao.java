package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.AppointmentDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class MySqlAppointmentDao extends GenericDao<Appointment> implements AppointmentDao {

    private static MySqlAppointmentDao instance;

    public static AppointmentDao getInstance() {
        if (instance == null) {
            instance = new MySqlAppointmentDao();
        }
        return instance;
    }

    private MySqlAppointmentDao(){}

    @Override
    protected Appointment mapToEntity(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("id"));
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            appointment.setStartDate(startDate);
        }
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            appointment.setEndDate(endDate);
        }
        appointment.setTitle(rs.getString("title"));
        appointment.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
        appointment.setType(AppointmentType.valueOf(rs.getString("type")));
        long hospitalCardId = rs.getLong("hospital_card_id");
        if (hospitalCardId > 0) {
            HospitalCard hs = new HospitalCard();
            hs.setId(hospitalCardId);
            appointment.setHospitalCard(hs);
        }
        long userId = rs.getLong("user_id");
        if (userId > 0) {
            User user = new User();
            user.setId(userId);
            appointment.setUser(user);
        }
        return appointment;
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, Appointment appointment) throws SQLException {
        int k = 1;
        if (appointment.getStartDate() != null) {
            ps.setDate(k++, Date.valueOf(appointment.getStartDate().toString()));
        } else {
            ps.setNull(k++, Types.DATE);
        }
        if (appointment.getEndDate() != null) {
            ps.setDate(k++, Date.valueOf(appointment.getEndDate().toString()));
        } else {
            ps.setNull(k++, Types.DATE);
        }
        ps.setString(k++, appointment.getTitle());
        ps.setString(k++, appointment.getStatus().name());
        ps.setString(k++, appointment.getType().name());
        ps.setLong(k++, appointment.getHospitalCard().getId());
        if (appointment.getUser() != null) {
            ps.setLong(k, appointment.getUser().getId());
        } else {
            ps.setNull(k, Types.INTEGER);
        }
    }


    @Override
    public long insertAppointment(Connection connection, Appointment appointment) {
        return insertEntity(connection, "INSERT INTO appointment" +
                " (start_date, end_date, title, status, type, hospital_card_id, user_id)" +
                " VALUES (?,?,?,?,?,?,?)", appointment);
    }

    @Override
    public void updateAppointment(Connection connection, Appointment appointment) {
        updateEntity(connection, "UPDATE appointment SET " +
                "start_date = ?, end_date = ?, title = ?, status = ?, type = ?, hospital_card_id = ?, user_id = ?" +
                " WHERE id =" + appointment.getId(), appointment);
    }

    @Override
    public Appointment findAppointmentByHospitalCardId(Connection connection, long id) {
        return findEntity(connection, "SELECT * FROM appointment WHERE hospital_card_id = ?", id);
    }

    @Override
    public Appointment findAppointmentById(Connection connection, long id) {
        return findEntity(connection, "SELECT * FROM appointment WHERE id = ?", id);
    }

    @Override
    public List<Appointment> findAppointmentsByType(Connection connection, List<String> types, int offset, int noOfRecords) {
        long c = types.stream().filter(Objects::isNull).count();
        if (c == 3 && types.size() == 3) return findEntities(connection, "SELECT SQL_CALC_FOUND_ROWS * FROM appointment" + " LIMIT " + offset + ", " + noOfRecords);
        if (c == 3 && types.size() == 4) return findEntities(connection, "SELECT SQL_CALC_FOUND_ROWS * FROM appointment WHERE status = ?" + " LIMIT " + offset + ", " + noOfRecords, types.get(3));
        return findEntities(connection, "SELECT SQL_CALC_FOUND_ROWS * FROM appointment WHERE (type=? OR type=? OR type=?)" + (types.size() > 3 ? " AND status = ?" : " ") + " LIMIT " + offset + ", " + noOfRecords,  types.toArray(String[]::new));
    }

    @Override
    public int getNumberOfRecords() {
        return super.getNumberOfRecords();
    }

}
