package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GenericDao<T extends Entity>  {

    private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);

    private int noOfRecords;

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
            rs.close();
            rs = ps.executeQuery("SELECT FOUND_ROWS()");
            if (rs.next()) {
                this.noOfRecords = rs.getInt(1);
            }
            return list;
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        } finally {
            closeResultSetAndPreparedStatement(rs, ps);
        }
    }

    protected int getNumberOfRecords() {
        return noOfRecords;
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
            if (rs != null) {
                rs.close();
            }
            rs = ps.executeQuery("SELECT FOUND_ROWS()");
            if (rs.next()) {
                this.noOfRecords = rs.getInt(1);
            }
            return list;
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeResultSetAndPreparedStatement(rs, ps);
        }
    }

    @SafeVarargs
    private <V> ResultSet setResultSetValues(PreparedStatement ps, V... values) throws SQLException {
        ResultSet rs = null;
        int pos = 1;
        for (V value : values) {
            switch (value.getClass().getSimpleName()) {
                case "Integer" -> ps.setInt(pos, (Integer) value);
                case "Long" -> ps.setLong(pos, (Long) value);
                case "String" -> ps.setString(pos, (String) value);
                case "Date" -> ps.setDate(pos, (Date) value);
                default -> throw new IllegalArgumentException();
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
            throw new EntityNotFoundException("not.found");
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeResultSetAndPreparedStatement(rs, ps);
        }
    }

    protected long insertEntity(Connection connection, String sql, T item) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            mapFromEntity(ps, item);
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new InputErrorsMessagesException(Map.of("insert_error","sql.insert"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        } finally {
            closeResultSetAndPreparedStatement(rs, ps);
        }
    }

    protected void deleteEntity(Connection connection, String sql) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DBExceptionMessages(List.of("sql.data_integrity"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeResultSetAndPreparedStatement(null, ps);
        }
    }

    protected <V> void updateByField(Connection connection, String sql, T item, int parameterIndex, V value) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            switch (value.getClass().getSimpleName()) {
                case "Integer" -> ps.setInt(parameterIndex, (Integer) value);
                case "String" -> ps.setString(parameterIndex, (String) value);
                default -> throw new IllegalArgumentException();
            }
            mapFromEntity(ps, item);
            if (ps.executeUpdate() == 0)
                throw new DBExceptionMessages(List.of("sql.not_updated"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeResultSetAndPreparedStatement(null, ps);
        }
    }

    protected void updateEntity(Connection connection, String sql, T item) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            mapFromEntity(ps, item);
            if (ps.executeUpdate() == 0)
                throw new DBExceptionMessages(List.of("sql.not_updated"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    protected abstract T mapToEntity(ResultSet rs) throws SQLException;

    protected abstract void mapFromEntity(PreparedStatement statement, T entity) throws SQLException;

    protected void closeResultSetAndPreparedStatement(ResultSet rs, Statement statement) {
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
    }
}
