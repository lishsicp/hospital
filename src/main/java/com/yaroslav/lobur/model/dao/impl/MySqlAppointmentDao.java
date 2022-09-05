package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.AppointmentDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.*;
import com.yaroslav.lobur.model.entity.enums.*;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class MySqlAppointmentDao extends GenericDao<Appointment> implements AppointmentDao {

    private static final String QUERY_WITH_JOINS = "FROM appointment a\n" +
            "    LEFT JOIN hospital_card hc on a.hospital_card_id = hc.id\n" +
            "    LEFT JOIN patient p on hc.patient_id = p.id\n" +
            "    LEFT JOIN doctor d on p.doctor_id = d.id\n" +
            "    LEFT JOIN user u on d.user_id = u.id\n" +
            "    LEFT JOIN category c on d.category_id = c.id\n" +
            "    LEFT JOIN user u2 on a.user_id = u2.id ";

    @Override
    protected Appointment mapToEntity(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("a.id"));
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            appointment.setStartDate(startDate);
        }
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            appointment.setEndDate(endDate);
        }
        appointment.setTitle(rs.getString("title"));
        appointment.setStatus(AppointmentStatus.valueOf(rs.getString("a.status")));
        appointment.setType(AppointmentType.valueOf(rs.getString("type")));
        long hospitalCardId = rs.getLong("hospital_card_id");
        if (hospitalCardId > 0) {
            long patientId = rs.getLong("patient_id");
            HospitalCard hs = new HospitalCard();
            hs.setId(hospitalCardId);
            hs.setDiagnosis(rs.getString("diagnosis"));
            if (patientId > 0) {
                Patient patient = new Patient();
                patient.setId(patientId);
                patient.setStatus(PatientStatus.valueOf(rs.getString("p.status")));
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
                    user.setPhone(rs.getString("u.phone"));
                    user.setAddress(rs.getString("u.address"));
                    user.setLocale(Locale.UK);
                    user.setRole(Role.getById(rs.getInt("u.role_id")));
                    doctor.setUser(user);
                    doctor.setCategory(category);
                    patient.setDoctor(doctor);
                    hs.setPatient(patient);
                }
                appointment.setHospitalCard(hs);
            }
        }
        long userId = rs.getLong("a.user_id");
        if (userId > 0) {
            User user = new User();
            user.setId(userId);
            user.setId(rs.getLong("u2.id"));
            user.setLogin("");
            user.setPassword("");
            user.setFirstname(rs.getString("u2.firstname"));
            user.setLastname(rs.getString("u2.lastname"));
            user.setDateOfBirth(rs.getDate("u2.date_of_birth"));
            user.setGender(rs.getString("u2.gender"));
            user.setEmail(rs.getString("u2.email"));
            user.setPhone(rs.getString("u2.phone"));
            user.setAddress(rs.getString("u2.address"));
            user.setLocale(Locale.UK);
            user.setRole(Role.getById(rs.getInt("u2.role_id")));
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
    public List<Appointment> findAppointmentsByHospitalCardId(Connection connection, long id) {
        return findEntities(connection,
                "SELECT SQL_CALC_FOUND_ROWS *\n" +
                        QUERY_WITH_JOINS +
                        "WHERE hospital_card_id = ?"
                , id);
    }

    @Override
    public Appointment findAppointmentById(Connection connection, long id) {
        return findEntity(connection,
                "SELECT *\n" +
                    QUERY_WITH_JOINS +
                "WHERE a.id = ?", id);
    }

    @Override
    public List<Appointment> findAppointmentsByType(Connection connection, List<String> types, int offset, int noOfRecords) {
        long c = types.stream().filter(Objects::isNull).count();
        String query = "SELECT SQL_CALC_FOUND_ROWS *\n" +
                QUERY_WITH_JOINS;
        if (c == 3 && types.size() == 3) return findEntities(connection, query + " LIMIT " + offset + ", " + noOfRecords);
        if (c == 3 && types.size() == 4) return findEntities(connection, query +  " WHERE a.status = ?" + " LIMIT " + offset + ", " + noOfRecords, types.get(3));
        return findEntities(connection, query +  "WHERE (type=? OR type=? OR type=?)" + (types.size() > 3 ? " AND a.status = ?" : " ") + " LIMIT " + offset + ", " + noOfRecords,  types.toArray(String[]::new));
    }

    @Override
    public int getNumberOfRecords() {
        return super.getNumberOfRecords();
    }

}
