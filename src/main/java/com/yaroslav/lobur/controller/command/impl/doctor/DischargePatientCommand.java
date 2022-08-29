package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.PatientStatus;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class DischargePatientCommand implements Command {

    private static final Logger logger = Logger.getLogger(DischargePatientCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        long patientToDischarge = NumberUtils.toLong(request.getParameter("discharge"));
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        try {
            Patient patient = patientService.getPatientById(patientToDischarge);
            patient.setStatus(PatientStatus.DISCHARGED);
            patientService.updatePatient(patient);
            session.setAttribute("success", "doctor.view_patient.discharged");
            long cardId = NumberUtils.toLong(request.getParameter("cardId"));
            return new CommandResult(request.getRequestURI() + "?action=view_patient&cardId=" + cardId, true);
        } catch (UnknownSqlException | InputErrorsMessagesException | EntityNotFoundException e) {
            request.setAttribute("fail", "doctor.view_patient.not_discharged");
            logger.debug("Patient wasn't discharged");
            return new CommandResult("/viewPatient.jsp");
        }
    }
}
