package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.entity.Category;
import db.MySqlDatasource;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MySqlCategoryDaoTest {

    DataSource dataSource = MySqlDatasource.getDataSource();
    DaoFactory daoFactory = new MySqlDaoFactory(dataSource);
    CategoryDao categoryDao = daoFactory.getCategoryDao();

    @Test
    void testFindAllCategories() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Set<Category> treeSet = new HashSet<>(categoryDao.findAllCategories(con));
            assertEquals(4, treeSet.size());
        }
    }

    @Test
    void testFindCategoryById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Category category = new Category();
            category.setId(3);
            category.setName("surgeon");

            Category categoryDb = categoryDao.findCategoryById(con, category.getId());
            assertEquals(category.getName(), categoryDb.getName());
            assertEquals(category, categoryDb);
            assertEquals(category.toString(), categoryDb.toString());
            assertEquals(category.hashCode(), categoryDb.hashCode());
        }
    }

    @Test
    void testInsertCategory() throws SQLException {
        try (Connection con = daoFactory.open()) {
            Category category = new Category();
            category.setId(4);
            category.setName("Test");
            assertEquals(category.getId(), categoryDao.insertCategory(con, category));
        }
    }
}