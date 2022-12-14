package com.yaroslav.lobur.controller.listener;

import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.dao.*;
import com.yaroslav.lobur.model.dao.impl.MySqlDaoFactory;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.service.UserService;
import com.yaroslav.lobur.utils.SecurityMapInitializer;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


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
        initializeSecurityMap(ctx);
        logger.trace("Security initialized");
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
        //DaoFactory
        DaoFactory daoFactory = new MySqlDaoFactory(dataSource);
        // DAO
        UserDao userDao = daoFactory.getUserDao();
        PatientDao patientDao = daoFactory.getPatientDao();
        HospitalCardDao hospitalCardDao = daoFactory.getHospitalCardDao();
        DoctorDao doctorDao = daoFactory.getDoctorDao();
        AppointmentDao appointmentDao = daoFactory.getAppointmentDao();
        CategoryDao categoryDao = daoFactory.getCategoryDao();
        // SERVICE
        UserService userService = new UserService(daoFactory, userDao);
        PatientService patientService = new PatientService(daoFactory, patientDao, hospitalCardDao);
        DoctorService doctorService = new DoctorService(daoFactory, userDao, categoryDao, doctorDao);
        AppointmentService appointmentService = new AppointmentService(daoFactory, appointmentDao);
        // Attributes
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("patientService", patientService);
        ctx.setAttribute("doctorService", doctorService);
        ctx.setAttribute("appointmentService", appointmentService);
    }

    public void initializeLogger(ServletContext ctx) {
        String log4jConfigFile = ctx.getInitParameter("log4j-config");
        String fullPath = ctx.getRealPath("") + File.separator + log4jConfigFile;
        DOMConfigurator.configure(fullPath);
        logger.info("Application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute(DATASOURCE_PARAM);
        servletContext.removeAttribute("userService");
        servletContext.removeAttribute("patientService");
        servletContext.removeAttribute("doctorService");
        servletContext.removeAttribute("appointmentService");
    }

    public void initializeSecurityMap(ServletContext ctx) {
        Properties properties = new Properties();
        String securityConfigFile = ctx.getInitParameter("security-config");
        String fullPath = ctx.getRealPath("") + File.separator + securityConfigFile;
        try (FileInputStream fis = new FileInputStream(fullPath);) {
            properties.load(fis);
            ctx.setAttribute("securityMap", SecurityMapInitializer.initialize(properties));
        } catch (IOException e) {
            logger.error("Security wasn't initialized, Properties not found" + e);
            throw new UnknownSqlException("sql.error");
        }
    }
}
