package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Category;

import java.sql.Connection;
import java.util.List;

public interface CategoryDao {
    List<Category> findAllCategories(Connection connection);
    Category findCategoryById(Connection connection, long id);
}
