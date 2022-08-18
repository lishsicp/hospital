package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.*;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Gender;
import com.yaroslav.lobur.model.entity.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlUserDao extends GenericDao<User> implements UserDao {

    public MySqlUserDao(DataSource ds) {
        super(ds);
    }

    private static final String FIND_ALL_USERS = "SElECT * FROM hospital.user";
    private static final String INSERT_USER = "INSERT INTO hospital.user(`login`, `password`, `firstname`, `lastname`, `date_of_birth`, `gender`, `email`, `phone`, `address`, `locale`, `role_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String FIND_USER_BY_EMAIL = "SELECT * FROM hospital.user WHERE email = ?";
    private static final String CHECK_UNIQUE_FIELDS = "SELECT count(id) FROM user WHERE login = ? AND id != ? UNION ALL " +
            "SELECT count(id) FROM user WHERE email = ? AND id != ? UNION ALL " +
            "SELECT count(id) FROM user WHERE phone = ? AND id != ?";
    private static final String FIND_ALL_BY_ROLE = "SELECT * FROM user WHERE role_id = ?";

    @Override
    public List<User> findAllUsers()  {
        return findAll(getConnection(), FIND_ALL_USERS);
    }

    @Override
    public List<User> findAllByRole(Role role) {
        return findEntities(getConnection(), FIND_ALL_BY_ROLE, role.getRoleId());
    }

    @Override
    public User findUserById(Long id) {
        return findEntity(getConnection(), "SELECT * FROM user WHERE id = ?", id);
    }

    @Override
    public User findUserByEmail(String email) {
        return findEntity(getConnection(), FIND_USER_BY_EMAIL, email);
    }

    @Override
    public void updateUser(User user) {
        updateByField(getConnection(), "", user, 6, user.getId());
    }

    @Override
    public void deleteUser(long id) {
        deleteEntity(getConnection(), "DELETE FROM user WHERE id=" + id);
    }

    @Override
    public long insertUser(User user) {
        return insertEntity(getConnection(), INSERT_USER, user);
//        ResultSet resultSet = null;
//        try(
//            Connection con = ConnectionProvider.getInstance().getConnection();
//            PreparedStatement ps = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
//            TransactionManager.start();
//            mapFromEntity(ps, user);
//            if (ps.executeUpdate() > 0) {
//                resultSet = ps.getGeneratedKeys();
//                if (resultSet.next()) {
//                    user.setId(resultSet.getLong(1));
//                    logger.debug("User has been added: {}", user);
//                    TransactionManager.commit();
//                    return user.getId();
//                }
//            }
//            throw new UnknownSqlException();
//        } catch (SQLException e) {
//            TransactionManager.rollback();
//            logger.error("SQLException thrown when trying to insert a user {}", e.getMessage());
//            throw new UnknownSqlException(e.getMessage());
//        } finally {
//            if (resultSet != null) {
//                try {
//                    resultSet.close();
//                } catch (SQLException ignored) {}
//            }
//        }
    }

    @Override
    public void updatePassword(User user) {
        return;
    }

    @Override
    public void checkUniqueFields(User user) {
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(CHECK_UNIQUE_FIELDS)) {
            ps.setString(1, user.getLogin());
            ps.setLong(2, user.getId());
            ps.setString(3, user.getEmail());
            ps.setLong(4, user.getId());
            ps.setString(5, user.getPhone());
            ps.setLong(6, user.getId());
            ResultSet rs = ps.executeQuery();
            Map<String, String> errors = new HashMap<>();
            rs.next();
            if (rs.getInt(1) > 0) {
                //errors.put("login", "validation.user.login.exist");
                errors.put("login", "validation.user.login.exist");
            }
            rs.next();
            if (rs.getInt(1) > 0) {
                errors.put("email", "validation.user.email.exist");
            }
            rs.next();
            if (rs.getInt(1) > 0) {
                errors.put("phone", "validation.user.phone.exist");
            }
            if (!errors.isEmpty())
                throw new InputErrorsMessagesException(errors);
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected User mapToEntity(ResultSet rs) throws SQLException {
        User user = new User();
        long id = rs.getLong("id");
        String login = rs.getString("login");
        String password = rs.getString("password");
        String firstname = rs.getString("firstname");
        String lastname = rs.getString("lastname");
        Date dateOfBirth = rs.getDate("date_of_birth");
        String gender = rs.getString("gender");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        int roleId = rs.getInt("role_id");

        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(Gender.valueOf(gender));
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(Role.getById(roleId));
        return user;
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getLogin());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstname());
        ps.setString(4, user.getLastname());
        ps.setDate(5, java.sql.Date.valueOf(user.getDateOfBirth().toString()));
        ps.setString(6, user.getGender().name());
        ps.setString(7, user.getEmail());
        ps.setString(8, user.getPhone());
        ps.setString(9, user.getAddress());
        ps.setString(10, user.getLocale().name());
        ps.setLong(11, user.getRole().getRoleId());
    }
}
