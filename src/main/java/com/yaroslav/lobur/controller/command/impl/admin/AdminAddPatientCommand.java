package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.Gender;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.service.UserService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import com.yaroslav.lobur.validator.PatientValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Date;
import java.util.Map;

public class AdminAddPatientCommand implements Command {
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
        patient.setStatus("New patient");
        Map<String, String> errors = PatientValidator.getInstance().validate(patient);
        if (errors.isEmpty()) {
            PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
            try {
                patientService.addPatient(patient);
            } catch (InputErrorsMessagesException e) {
                errors.putAll(e.getErrorMessageMap());
            }
        }
        if (!errors.isEmpty()) {
            session.setAttribute("userErrors", errors);
        }
        return new CommandResult(request.getHeader("referer"), true);
    }
}
