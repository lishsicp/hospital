package com.yaroslav.lobur.service;

import com.yaroslav.lobur.model.dao.DaoFactory;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.model.entity.enums.Role;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import com.yaroslav.lobur.validator.DoctorValidator;
import db.MySqlDatasource;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoctorServiceTest {

    private static DoctorService doctorService;

    @BeforeAll
    static void setUp() {
        DaoFactory.init(MySqlDatasource.getDataSource());
        doctorService = new DoctorService();
    }

    @Test
    @Order(3)
    void getAllDoctors() {
        List<Doctor> doctorList = doctorService.getAllDoctors();
        assertEquals(4, doctorList.size());
        assertEquals(doctorList.size(), doctorService.getNumberOfRecords());
    }

    @Test
    void getDoctorsByCategory() {
        List<Doctor> doctorList = doctorService.getDoctorsByCategory(3);
        long countSurgeons = doctorList.stream().filter(d -> d.getCategory().getId() == 3).count();
        assertEquals(1, countSurgeons);
        assertEquals(doctorList.size(), countSurgeons);
    }

    @Test
    void getAllDoctorsOrderBy() {
        List<Doctor> doctorList = doctorService.getAllDoctorsOrderBy(OrderBy.NUMBER_OF_PATIENTS, 0,100);
        List<Doctor> doctorListSorted = new ArrayList<>(doctorList);
        assertEquals(doctorList, doctorListSorted);
    }

    @Test
    void getAllCategories() {
        List<Category> categories = doctorService.getAllCategories();
        assertEquals(3, categories.size());
    }

    @Test
    @Order(1)
    void addDoctor() {
        User user = new User();
        user.setLogin("login");
        user.setPassword(PasswordEncryptor.getSHA1String("Password1"));
        user.setFirstname("Test");
        user.setLastname("Test");
        user.setDateOfBirth(java.sql.Date.valueOf(LocalDate.now()));
        user.setGender("MALE");
        user.setEmail("test@gg.com");
        user.setPhone("(000)-000-00-00");
        user.setLocale(Locale.UK);
        user.setAddress("");
        user.setRole(Role.DOCTOR);
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        Category category = new Category();
        category.setId(1);
        doctor.setCategory(category);
        doctorService.addDoctor(doctor);
        Doctor doctor1 = doctorService.getDoctorByUser(user);
        doctor.setId(doctor1.getId());
        assertEquals(doctor, doctor1);
        user.setPassword("Password1");
        assertEquals(0, DoctorValidator.getInstance().validate(doctor).size());
    }
}