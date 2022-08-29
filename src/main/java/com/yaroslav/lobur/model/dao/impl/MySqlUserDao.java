package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.exceptions.*;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlUserDao extends GenericDao<User> implements UserDao {

    private static final String FIND_ALL_USERS = "SElECT * FROM user";
    private static final String INSERT_USER = "INSERT INTO user(`login`, `password`, `firstname`, `lastname`, `date_of_birth`, `gender`, `email`, `phone`, `address`, `locale`, `role_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String FIND_USER_BY_EMAIL = "SELECT * FROM user WHERE email = ?";
    private static final String CHECK_UNIQUE_FIELDS = "SELECT count(id) FROM user WHERE login = ? AND id != ? UNION ALL " +
            "SELECT count(id) FROM user WHERE email = ? AND id != ? UNION ALL " +
            "SELECT count(id) FROM user WHERE phone = ? AND id != ?";
    private static final String FIND_ALL_BY_ROLE = "SELECT * FROM user WHERE role_id = ?";

    private static MySqlUserDao instance;

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new MySqlUserDao();
        }
        return instance;
    }

    private MySqlUserDao(){}

    @Override
    public List<User> findAllUsers(Connection connection)  {
        return findAll(connection, FIND_ALL_USERS);
    }

    @Override
    public List<User> findAllByRole(Connection connection, Role role) {
        return findEntities(connection, FIND_ALL_BY_ROLE, role.getRoleId());
    }

    @Override
    public User findUserById(Connection connection, Long id) {
        return findEntity(connection, "SELECT * FROM user WHERE id = ?", id);
    }

    @Override
    public User findUserByEmail(Connection connection, String email) {
        return findEntity(connection, FIND_USER_BY_EMAIL, email);
    }

    @Override
    public void updateUser(Connection connection, User user) {
        updateEntity(connection, "UPDATE user SET  login = ?, password = ?, firstname = ?, lastname = ?, date_of_birth = ?, gender = ?, email = ?, phone = ?, address = ?, locale = ?, role_id = ? WHERE id = " + user.getId(), user);
    }

    @Override
    public void deleteUser(Connection connection, long id) {
        deleteEntity(connection, "DELETE FROM user WHERE id=" + id);
    }

    @Override
    public long insertUser(Connection connection, User user) {
        return insertEntity(connection, INSERT_USER, user);
    }

    @Override
    public void updatePassword(Connection connection, User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkUniqueFields(Connection con, User user) {
        try (PreparedStatement ps = con.prepareStatement(CHECK_UNIQUE_FIELDS)) {
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
        String locale = rs.getString("locale");

        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setLocale(Locale.valueOf(locale));
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
        ps.setString(6, user.getGender());
        ps.setString(7, user.getEmail());
        ps.setString(8, user.getPhone());
        ps.setString(9, user.getAddress());
        ps.setString(10, user.getLocale().name());
        ps.setLong(11, user.getRole().getRoleId());
    }
}
