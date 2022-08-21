package com.yaroslav.lobur.service;

import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    private static final UserDao userDao;
    private static final CategoryDao categoryDao;
    private static final DoctorDao doctorDao;

    private static final DaoFactory daoFactory;

    static {
        daoFactory = DaoFactory.getDaoFactory();
        userDao = daoFactory.getUserDao();
        categoryDao = daoFactory.getCategoryDao();
        doctorDao = daoFactory.getDoctorDao();
    }

//    public List<Doctor> getAllDoctors() {
//        try {
//            Connection con = daoFactory.open();
//            List<Doctor> doctors = doctorDao.findAllDoctors(daoFactory.open());
//            setDoctorFields(doctors);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//        return doctors;
//    }

    private void setDoctorFields(List<Doctor> doctors, List<User> users, List<Category> categories) {
        for (Doctor d : doctors) {
            for (User u : users) {
                if (d.getUser().getId() == u.getId()) {
                    d.setUser(u);
                }
            }
            for (Category c : categories) {
                if (d.getCategory().getId() == c.getId()) {
                    d.setCategory(c);
                }
            }
        }
    }

    public List<Doctor> getAllDoctorsOrderBy(OrderBy order, int offset, int noOfRecords) {
        Connection con = daoFactory.open();
        List<Doctor> doctors;
        List<User> users;
        List<Category> categories;
        try {
            doctors = doctorDao.findDoctorsOrderBy(con, order, offset, noOfRecords);
            users = userDao.findAllByRole(con, Role.DOCTOR);
            categories = categoryDao.findAllCategories(con);
            setDoctorFields(doctors, users, categories);
        } finally {
            daoFactory.close(con);
        }
        return doctors;
    }

    public int getNumberOfRecords() {
        return doctorDao.getNoOfRecords();
    }

    public List<Category> getAllCategories() {
        return categoryDao.findAllCategories(daoFactory.open());
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


}
