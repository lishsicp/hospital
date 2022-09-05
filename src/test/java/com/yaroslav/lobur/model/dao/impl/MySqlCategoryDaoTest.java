package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.dao.PatientDao;
import db.MySqlDatasource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlCategoryDaoTest {

    DataSource dataSource = MySqlDatasource.getDataSource();
    DaoFactory daoFactory = new MySqlDaoFactory(dataSource);
    CategoryDao categoryDao = daoFactory.getCategoryDao();

    @Test
    void testFindAllCategories() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertEquals(3, categoryDao.findAllCategories(con).size());
        }
    }

    @Test
    void testFindCategoryById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertEquals("surgeon", categoryDao.findCategoryById(con, 3).getName());
        }
    }
}