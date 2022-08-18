package com.yaroslav.lobur.service;

import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.DoctorDao;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.Role;

import java.util.List;

public class DoctorService {

    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final DoctorDao doctorDao;

    public DoctorService(UserDao userDao, CategoryDao categoryDao, DoctorDao doctorDao) {
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.doctorDao = doctorDao;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = doctorDao.findAllDoctors();
        setDoctorFields(doctors);
        return doctors;
    }

    private void setDoctorFields(List<Doctor> doctors) {
        List<User> users = userDao.findAllByRole(Role.DOCTOR);
        List<Category> categories = categoryDao.findAllCategories();
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

    public List<Doctor> getAllDoctorsOrderBy(OrderBy order) {
        List<Doctor> doctors = doctorDao.findDoctorsOrderBy(order);
        setDoctorFields(doctors);
        return doctors;
    }


}
