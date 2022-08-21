package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.GenericDao;
import com.yaroslav.lobur.model.entity.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlCategoryDao extends GenericDao<Category> implements CategoryDao {

    private static MySqlCategoryDao instance;

    public static MySqlCategoryDao getInstance() {
        if (instance == null) {
            instance = new MySqlCategoryDao();
        }
        return instance;
    }

    private MySqlCategoryDao(){}

    @Override
    protected Category mapToEntity(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("category"));
        return category;
    }

    @Override
    protected void mapFromEntity(PreparedStatement ps, Category category) throws SQLException {
        ps.setLong(1, category.getId());
        ps.setString(2, category.getName());
    }

    public List<Category> findAllCategories(Connection connection) {
        return findAll(connection, "SELECT * FROM category");
    }

    public Category findCategoryById(Connection connection, long id) {
        return findEntity(connection, "SELECT * FROM category WHERE id=?", id);
    }

}
