package com.yaroslav.lobur.controller.command.impl;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class MakeAppointmentCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(MakeAppointmentCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        long appointmentId = NumberUtils.toLong(request.getParameter("appointmentId"));
        User currentUser = (User) session.getAttribute("current_user");
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            appointment.setUser(currentUser);
            appointment.setStatus(AppointmentStatus.DONE);
            appointment.setEndDate(java.sql.Date.valueOf(LocalDate.now()));
            appointmentService.updateAppointment(appointment);
        } catch (EntityNotFoundException e) {
            logger.error("", e);
            request.setAttribute("sql", e.getMessage());
        } catch (InputErrorsMessagesException e) {
            logger.error("{}", e.getErrorMessageMap().values());
            request.setAttribute("errors", e.getErrorMessageMap());
        } catch (UnknownSqlException e) {
            logger.error("{}", e.getMessage());
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult(request.getRequestURI() + "?action=appointments", true);
    }
}
