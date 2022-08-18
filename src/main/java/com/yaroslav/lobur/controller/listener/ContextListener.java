package com.yaroslav.lobur.controller.listener;

import com.yaroslav.lobur.model.dao.CategoryDao;
import com.yaroslav.lobur.model.dao.DoctorDao;
import com.yaroslav.lobur.model.dao.PatientDao;
import com.yaroslav.lobur.model.dao.UserDao;
import com.yaroslav.lobur.model.dao.impl.MySqlCategoryDao;
import com.yaroslav.lobur.model.dao.impl.MySqlDoctorDao;
import com.yaroslav.lobur.model.dao.impl.MySqlPatientDao;
import com.yaroslav.lobur.model.dao.impl.MySqlUserDao;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;

@WebListener
public class ContextListener implements ServletContextListener, HttpSessionListener {

    private static final Logger logger = org.apache.log4j.Logger.getLogger(ContextListener.class);

    private static final String DATASOURCE_PARAM = "dataSource";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        initializeLogger(ctx);
        logger.debug("Logger initialized");
        logger.trace("Context initialization started");
        initDatasource(ctx);
        logger.debug("DataSource initialized");
        initServices(ctx);
        logger.debug("Services initialized");
    }

    public void initDatasource(ServletContext ctx) {
        String dsName = ctx.getInitParameter(DATASOURCE_PARAM);
        Context jndiContext;
        try {
            jndiContext = (Context) new InitialContext().lookup("java:/comp/env");
            DataSource ds = (DataSource) jndiContext.lookup(dsName);
            ctx.setAttribute(DATASOURCE_PARAM, ds);
            logger.trace("Context dataSource is set to " + ds.getClass().getName());
        } catch (NamingException e) {
            throw new IllegalStateException("DataSource initialization error", e);
        }
    }

    public void initServices(ServletContext ctx) {
        DataSource dataSource = (DataSource) ctx.getAttribute(DATASOURCE_PARAM);
        // DAO
        UserDao userDao = new MySqlUserDao(dataSource);
        PatientDao patientDao = new MySqlPatientDao(dataSource);
        CategoryDao categoryDao = new MySqlCategoryDao(dataSource);
        DoctorDao doctorDao = new MySqlDoctorDao(dataSource);
        // SERVICE
        UserService userService = new UserService(userDao);
        PatientService patientService = new PatientService(patientDao);
        DoctorService doctorService = new DoctorService(userDao, categoryDao, doctorDao);
        // Attributes
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("patientService", patientService);
        ctx.setAttribute("doctorService", doctorService);
    }

    public void initializeLogger(ServletContext ctx) {
        String log4jConfigFile = ctx.getInitParameter("log4j-config");
        String fullPath = ctx.getRealPath("") + File.separator + log4jConfigFile;
        DOMConfigurator.configure(fullPath);
        logger.info("Application started");
    }
}
