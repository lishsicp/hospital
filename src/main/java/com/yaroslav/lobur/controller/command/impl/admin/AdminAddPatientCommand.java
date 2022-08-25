package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import com.yaroslav.lobur.validator.PatientValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Map;

public class AdminAddPatientCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(AdminAddPatientCommand.class);
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.admin.addPatient");
        if (request.getMethod().equals("GET")) {
            return new CommandResult(page);
        }
        session.setAttribute("currentPage", page);
        Patient patient = new Patient();
        patient.setFirstname(request.getParameter("firstName"));
        patient.setLastname(request.getParameter("lastName"));
        patient.setDateOfBirth(Date.valueOf(request.getParameter("date_of_birth")));
        patient.setGender(request.getParameter("gender"));
        patient.setEmail(request.getParameter("email"));
        patient.setDoctor(null);
        patient.setStatus(PatientStatus.NEW);
        Map<String, String> errors = PatientValidator.getInstance().validate(patient);
        if (errors.isEmpty()) {
            PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
            try {
                patientService.addPatient(patient);
            } catch (InputErrorsMessagesException e) {
                logger.error("Add patient error {}", e.getErrorMessageMap().values());
                errors.putAll(e.getErrorMessageMap());
            } catch (UnknownSqlException e) {
                logger.error("", e);
                errors.put("sql", e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            session.removeAttribute("success");
            request.setAttribute("errors", errors);
            return new CommandResult(page);
        } else {
            session.setAttribute("success", "admin.patient.success");
        }
        return new CommandResult(request.getContextPath() + page, true);
    }
}
