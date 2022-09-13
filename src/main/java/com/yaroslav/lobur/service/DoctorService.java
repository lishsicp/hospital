package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.OrderBy;

import java.sql.Connection;
import java.util.List;

public class DoctorService {
    private final DaoFactory daoFactory;

    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final DoctorDao doctorDao;

    public DoctorService(DaoFactory daoFactory, UserDao userDao, CategoryDao categoryDao, DoctorDao doctorDao) {
        this.daoFactory = daoFactory;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.doctorDao = doctorDao;
    }

    public List<Doctor> getAllDoctors() {
        Connection con = null;
        try {
            con = daoFactory.open();
            return doctorDao.findAllDoctors(con);
        } finally {
            daoFactory.close(con);
        }
    }

    public List<Doctor> getDoctorsByCategory(long categoryId) {
        Connection con = daoFactory.open();
        try {
            return doctorDao.findDoctorsByCategory(con, categoryId);
        } finally {
            daoFactory.close(con);
        }
    }

    public List<Doctor> getAllDoctorsOrderBy(OrderBy order, int offset, int noOfRecords) {
        Connection con = daoFactory.open();
        try {
            return doctorDao.findDoctorsOrderBy(con, order, offset, noOfRecords);
        } finally {
            daoFactory.close(con);
        }
    }

    public int getNumberOfRecords() {
        return doctorDao.getNumberOfRecords();
    }

    public List<Category> getAllCategories() {
        Connection con = null;
        try {
            con = daoFactory.open();
            return categoryDao.findAllCategories(con);
        } finally {
            daoFactory.close(con);
        }
    }

    public void addDoctor(Doctor doctor) {
        Connection connection = null;
        try {
            connection = daoFactory.beginTransaction();
            userDao.checkUniqueFields(connection, doctor.getUser());
            long userId = userDao.insertUser(connection, doctor.getUser());
            doctor.getUser().setId(userId);
            doctorDao.insertDoctor(connection, doctor);
            daoFactory.commit(connection);
        } catch (InputErrorsMessagesException | UnknownSqlException e) {
            daoFactory.rollback(connection);
            throw e;
        } finally {
            daoFactory.endTransaction(connection);
        }
    }

    public void addCategory(Category category) {
        Connection connection = null;
        try {
            connection = daoFactory.beginTransaction();
            categoryDao.insertCategory(connection, category);
            daoFactory.commit(connection);
        } catch (InputErrorsMessagesException | UnknownSqlException e) {
            daoFactory.rollback(connection);
            throw e;
        } finally {
            daoFactory.endTransaction(connection);
        }
    }

    public Doctor getDoctorByUser(User user) {
        Connection con = null;
        try {
            con = daoFactory.open();
            return doctorDao.findDoctorByUserId(con, user.getId());
        } finally {
            daoFactory.close(con);
        }
    }
}
