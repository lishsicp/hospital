package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssignDoctorCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(AssignDoctorCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        long patientId = NumberUtils.toLong(request.getParameter("patient_id"));
        long doctorId = NumberUtils.toLong(request.getParameter("doctor_id"));
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        try {
            Patient patient = patientService.getPatientById(patientId);
            Doctor doctor = new Doctor();
            doctor.setId(doctorId);
            patient.setDoctor(doctor);
            patient.setStatus(PatientStatus.TREATMENT);
            patientService.updatePatient(patient);
        } catch (UnknownSqlException | EntityNotFoundException e) {
            logger.error("Error when assigning doctor", e);
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult(request.getContextPath() + "/controller?action=list_patients", true);
    }
}
