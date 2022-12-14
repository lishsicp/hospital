package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.validator.AppointmentValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

public class UpdateAppointmentCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(UpdateAppointmentCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.view_patient");
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        Map<String, String> errors = null;
        try {
            String type = request.getParameter("appointment");
            String description = request.getParameter("description").trim();
            long appointmentId = NumberUtils.toLong(request.getParameter("appointmentId"));
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            appointment.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
            appointment.setStatus(AppointmentStatus.ONGOING);
            appointment.setType(AppointmentType.valueOf(type));
            appointment.setTitle(description);
            errors = AppointmentValidator.getInstance().validate(appointment);
            if (errors.isEmpty()) {
                appointmentService.updateAppointment(appointment);
                session.setAttribute("success", "appointment.updated");
                return new CommandResult(request.getContextPath() + "/controller?action=view_patient&cardId=" + appointment.getHospitalCard().getId(), true);
            } else {
                logger.debug("Appointment wasn't updated");
                logger.debug("{}", appointment);
                request.setAttribute("errors", errors);
            }
        } catch (EntityNotFoundException e) {
            logger.error("", e);
            request.setAttribute("sql", e.getMessage());
        } catch (InputErrorsMessagesException e) {
            logger.error("{}", e.getErrorMessageMap().values());
            if (errors != null) {
                errors.putAll(e.getErrorMessageMap());
                request.setAttribute("errors", errors);
            }
        } catch (UnknownSqlException e) {
            logger.error("{}", e.getMessage());
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult(page);
    }
}
