package com.yaroslav.lobur.model.dao;

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

/**
 * Generic DAO (Data Access Object) with most common methods.
 * @param <T> a type variable (Must implement <code>Entity</code> interface)
 */
public abstract class GenericDao<T extends Entity>  {

    private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);

    private int noOfRecords;

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     *
     * @param con a connection
     * @param sql SQL query <code>String</code>
     * @return List of populated entities
     */
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

    /**
     * @return Number of records in the table for given SQL query
     * (must add <tt>SQL_CALC_FOUND_ROWS</tt> to query string and execute
     * query with <code>SELECT FOUND_ROWS()</code> to assign value).
     */
    protected int getNumberOfRecords() {
        return noOfRecords;
    }

    /**
     * Generic method to get a List of entities based on values.
     * @param con a connection
     * @param sql SQL query <code>String</code>
     * @param values varargs for values to be inserted into SQL Query
     * @param <V> can be null, int, long, String or Date
     * @return List of populated entities based on values
     */
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

    /**
     * @param ps An object that represents a precompiled SQL statement.
     * @param values varargs for values to be inserted into SQL Query
     * @param <V> can be null, int, long, String or Date
     * @return filled Result Set based of values
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    @SafeVarargs
    private <V> ResultSet setResultSetValues(PreparedStatement ps, V... values) throws SQLException {
        ResultSet rs;
        int pos = 1;
        for (V value : values) {
            if (value == null) {
                ps.setNull(pos++, Types.VARCHAR);
                continue;
            }
            switch (value.getClass().getSimpleName()) {
                case "Integer" -> ps.setInt(pos, (Integer) value);
                case "Long" -> ps.setLong(pos, (Long) value);
                case "String" -> ps.setString(pos, (String) value);
                case "Date" -> ps.setDate(pos, (Date) value);
                default -> throw new IllegalArgumentException();
            }
            ++pos;
        }
        rs = ps.executeQuery();
        return rs;
    }

    /**
     * Generic method to get an object based on values. An
     * EntityNotFoundException Runtime Exception is thrown if
     * nothing is found.
     * @param con a connection
     * @param sql SQL query <code>String</code>
     * @param values varargs for values to be inserted into SQL Query
     * @param <V> can be null, int, long, String or Date
     * @return Single Entity from table based on values
     * @throws EntityNotFoundException if no entity was found
     * @throws UnknownSqlException wraps SQLException
     */
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

    /**
     * Generic method to save an Entity.
     * @param connection a connection
     * @param sql SQL query <code>String</code>
     * @param item Entity to be inserted
     * @return the id of inserted Entity
     * @throws InputErrorsMessagesException map of error messages
     * @throws UnknownSqlException wraps SQLException
     */
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
            throw new InputErrorsMessagesException(Map.of("sql","sql.insert"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage());
        } finally {
            closeResultSetAndPreparedStatement(rs, ps);
        }
    }

    /**
     * Generic method that remove entity
     * @param connection a connection
     * @param sql SQL query <code>String</code>
     * @throws InputErrorsMessagesException map of error messages
     * @throws UnknownSqlException wraps SQLException
     */
    protected void deleteEntity(Connection connection, String sql) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new InputErrorsMessagesException(Map.of("sql","sql.data_integrity"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        } finally {
            closeResultSetAndPreparedStatement(null, ps);
        }
    }

    /**
     * Generic method to update Entity.
     * @param connection a connection
     * @param sql SQL query <code>String</code>
     * @param item Entity to be updated
     * @throws InputErrorsMessagesException map of error messages
     * @throws UnknownSqlException wraps SQLException
     */
    protected void updateEntity(Connection connection, String sql, T item) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            mapFromEntity(ps, item);
            if (ps.executeUpdate() == 0)
                throw new InputErrorsMessagesException(Map.of("sql","sql.not_updated"));
        } catch (SQLException e) {
            throw new UnknownSqlException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Abstract method, that extracts entity fields from the current row of the provided ResultSet.
     * @param rs ResultSet
     * @return Entity with values set from ResultSet
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    protected abstract T mapToEntity(ResultSet rs) throws SQLException;

    /**
     * Abstract method, which is used for setting up PreparedStatement parameters, based on entity's fields values
     * @param statement PreparedStatement
     * @param entity Entity that PreparedStatement is filled from.
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    protected abstract void mapFromEntity(PreparedStatement statement, T entity) throws SQLException;

    /**
     * Closes ResultSet and Statement
     * @param rs ResultSet
     * @param statement Statement
     */
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
