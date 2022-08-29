package com.yaroslav.lobur.model.dao.impl;

import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.DaoFactory;
import db.MySqlDatasource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlCategoryDaoTest {

    static CategoryDao categoryDao;
    static DaoFactory daoFactory;

    @BeforeAll
    static void setUp() {
        DaoFactory.init(MySqlDatasource.getDataSource());
        daoFactory = DaoFactory.getDaoFactory();
        categoryDao = daoFactory.getCategoryDao();
    }

    @Test
    void findAllCategories() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertEquals(3, categoryDao.findAllCategories(con).size());
        }
    }

    @Test
    void findCategoryById() throws SQLException {
        try (Connection con = daoFactory.open()) {
            assertEquals("surgeon", categoryDao.findCategoryById(con, 3).getName());
        }
    }
}