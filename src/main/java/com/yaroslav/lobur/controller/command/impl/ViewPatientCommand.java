package com.yaroslav.lobur.controller.command.impl;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewPatientCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(ViewPatientCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.view_patient");
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        try {
            long hospitalCardId = NumberUtils.toLong(request.getParameter("cardId"));
            HospitalCard hospitalCard = patientService.getHospitalCardById(hospitalCardId);
            hospitalCard.setPatient(patientService.getPatientById(hospitalCard.getPatient().getId()));
            AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
            session.setAttribute("hospitalCard", hospitalCard);
            try {
                Appointment appointment = appointmentService.getAppointmentByHospitalCard(hospitalCard);
                logger.debug("{}", appointment);
                session.setAttribute("appointment", appointment);
            } catch (EntityNotFoundException e) {
                session.removeAttribute("appointment");
                logger.debug("No appointments found for patient {} {}", hospitalCard.getPatient().getFirstname(), hospitalCard.getPatient().getLastname());
            }
        } catch (EntityNotFoundException e) {
            logger.error("", e);
            session.setAttribute("sql", e.getMessage());
        } catch (UnknownSqlException e) {
            logger.error("", e);
            session.setAttribute("sql", "sql.error");
        }
        return new CommandResult(request.getContextPath() + page, true);
    }

}
