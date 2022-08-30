package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.validator.PatientValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;

import java.sql.Date;
import java.util.Map;

public class EditPatientCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.admin.patients");
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");

        Patient patient = new Patient();
        long patientId = NumberUtils.toLong(request.getParameter("patient_id"));
        if (patientId <= 0) {
            patient.setId(NumberUtils.toLong(request.getParameter("id")));
            patient.setFirstname(request.getParameter("firstName"));
            patient.setLastname(request.getParameter("lastName"));
            patient.setDateOfBirth(Date.valueOf(request.getParameter("date_of_birth")));
            patient.setGender(request.getParameter("gender"));
            patient.setEmail(request.getParameter("email"));
            patient.setStatus(PatientStatus.valueOf(request.getParameter("status")));
        } else {
            patient = patientService.getPatientById(patientId);
            patient.setStatus(PatientStatus.TREATMENT);
        }

        long doctorId = NumberUtils.toLong(request.getParameter("doctor_id"));

        if (doctorId <= 0) {
            patient.setDoctor(null);
        } else {
            Doctor d = new Doctor();
            d.setId(doctorId);
            patient.setDoctor(d);
        }

        Map<String, String> errors = PatientValidator.getInstance().validate(patient);
        if (errors.isEmpty()) {
            try {
                patientService.updatePatient(patient);
            } catch (InputErrorsMessagesException e) {
                errors.putAll(e.getErrorMessageMap());
            } catch (DBExceptionMessages e) {
                request.setAttribute("sql", e.getErrorMessages().get(0));
            }
        }
        if (!errors.isEmpty()) {
            session.removeAttribute("success");
            request.setAttribute("patientUpdateErrors", errors);
            request.setAttribute("fail", "patient.update.fail");
            return new CommandResult(page);
        } else {
            session.setAttribute("success", "patient.update.success");
        }
        return new CommandResult(request.getContextPath() + page, true);
    }
}
