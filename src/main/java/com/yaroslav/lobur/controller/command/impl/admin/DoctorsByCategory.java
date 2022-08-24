package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DoctorsByCategory implements Command {

    private static final Logger logger = LoggerFactory.getLogger(DoctorsByCategory.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        long categoryId = NumberUtils.toLong(request.getParameter("category_id"));
        long patientId = NumberUtils.toLong(request.getParameter("patient_id"));
        try {
            PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
            DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
            List<Doctor> doctorList = doctorService.getDoctorsByCategory(categoryId);
            logger.debug("{}", doctorList);
            Patient patient = patientService.getPatientById(patientId);
            logger.debug("{}", patient.getFirstname());
            session.setAttribute("patient", patient);
            session.setAttribute("doctorsByCategory", doctorList);
        } catch (UnknownSqlException e) {
            logger.debug("", e);
        }

        return new CommandResult("/assignPatient.jsp");
    }
}
