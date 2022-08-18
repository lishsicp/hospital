package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Entity;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T extends Entity> {

    private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);

    protected DataSource ds;

    protected GenericDao(DataSource ds) {
        this.ds = ds;
    }

    protected List<T> findAll(Connection con, String sql) {
        List<T> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapToEntity(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        } finally {
            closeAll(rs, ps, con);
        }
    }

    @SafeVarargs
    protected final <V> List<T> findEntities(Connection con, String sql, V... values) {
        List<T> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = setResultSetValues(ps, values);
            while (rs != null && rs.next()) {
                list.add(mapToEntity(rs));
            }
            if (list.isEmpty())
                throw new EntityNotFoundException("");
            return list;
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeAll(rs, ps, con);
        }
    }

    @SafeVarargs
    private <V> ResultSet setResultSetValues(PreparedStatement ps, V... values) throws SQLException {
        ResultSet rs = null;
        int pos = 1;
        for (V value : values) {
            switch (value.getClass().getSimpleName()) {
                case "Integer":
                    ps.setInt(pos, (Integer) value);
                    break;
                case "Long":
                    ps.setLong(pos, (Long) value);
                    break;
                case "String":
                    ps.setString(pos, (String) value);
                    break;
                case "Date":
                    ps.setDate(pos, (Date) value);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            ++pos;
            rs = ps.executeQuery();
        }
        return rs;
    }

    @SafeVarargs
    protected final <V> T findEntity(Connection con, String sql, V... values) {
        T entity;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = setResultSetValues(ps, values);
            if (rs != null && rs.next()) {
                entity = mapToEntity(rs);
                return entity;
            }
            throw new EntityNotFoundException("not found");
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeAll(rs, ps, con);
        }
    }

    protected long insertEntity(Connection connection, String sql, T item) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            mapFromEntity(ps, item);
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    connection.commit();
                    return rs.getLong(1);
                }
            }
            connection.rollback();
            throw new DBExceptionMessages(List.of("sql.insert"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        } finally {
            closeAll(rs, ps, connection);
        }
    }

    protected void deleteEntity(Connection connection, String sql) {
        try(Connection con = connection; PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DBExceptionMessages(List.of("sql.data_integrity"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    protected <V> void updateByField(Connection connection, String sql, T item, int parameterIndex, V value) {
        try (Connection con = connection; PreparedStatement ps = con.prepareStatement(sql)) {
            switch (value.getClass().getSimpleName()) {
                case "Integer":
                    ps.setInt(parameterIndex, (Integer) value);
                    break;
                case "String":
                    ps.setString(parameterIndex, (String) value);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            mapFromEntity(ps, item);
            if (ps.executeUpdate() == 0)
                throw new DBExceptionMessages(List.of("sql.not_updated"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    protected <V> void updateEntity(Connection connection, String sql, T item) {
        try (Connection con = connection; PreparedStatement ps = con.prepareStatement(sql)) {
            mapFromEntity(ps, item);
            if (ps.executeUpdate() == 0)
                throw new DBExceptionMessages(List.of("sql.not_updated"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    protected Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new UnknownSqlException("Error when getting a connection", e.getCause());
        }
    }
    protected abstract T mapToEntity(ResultSet rs) throws SQLException;

    protected abstract void mapFromEntity(PreparedStatement statement, T entity) throws SQLException;

    protected void closeAll(ResultSet rs, Statement statement, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
