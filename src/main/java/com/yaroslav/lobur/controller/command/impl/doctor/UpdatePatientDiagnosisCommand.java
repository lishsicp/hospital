package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.validator.HospitalCardValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePatientDiagnosisCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePatientDiagnosisCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.view_patient");
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        try {
            long hospitalCardId = NumberUtils.toLong(request.getParameter("cardId"));
            HospitalCard hospitalCard = patientService.getHospitalCardById(hospitalCardId);
            String diagnosis = request.getParameter("diagnosis");
            hospitalCard.setDiagnosis(diagnosis);
            var errors = HospitalCardValidator.getInstance().validate(hospitalCard);
            if (errors.isEmpty()) {
                patientService.updateHospitalCard(hospitalCard);
                session.setAttribute("success", "diagnosis.updated");
                HospitalCard h = (HospitalCard) session.getAttribute("hospitalCard");
                hospitalCard.setPatient(h.getPatient());
                session.setAttribute("hospitalCard", hospitalCard);
                return new CommandResult(request.getContextPath() + page, true);
            } else {
                logger.debug("Diagnosis wasn't updated");
                request.setAttribute("errors", errors);
            }
        } catch (EntityNotFoundException e) {
            logger.error("", e);
            request.setAttribute("sql", e.getMessage());
        } catch (UnknownSqlException e) {
            logger.error("{}", e.getMessage());
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult(page);
    }
}
