package com.yaroslav.lobur.controller.command.impl;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListAppointmentsCommand implements Command {

    private static final int NUMBER_OF_RECORDS_PER_PAGE = 5;

    private static final Logger logger = LoggerFactory.getLogger(ListAppointmentsCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String operation = request.getParameter("OPERATION");
        String medication = request.getParameter("MEDICATION");
        String procedure = request.getParameter("PROCEDURE");
        String status = request.getParameter("status");


        int pageNo = 1;
        if (request.getParameter("page") != null)
            pageNo = NumberUtils.toInt(request.getParameter("page"));

        int recordsPerPage = NumberUtils.toInt(request.getParameter("recordsPerPage"));
        recordsPerPage = recordsPerPage == 0 ? NUMBER_OF_RECORDS_PER_PAGE : recordsPerPage;
        request.setAttribute("recordsPerPage", recordsPerPage);

        List<String> types = Stream.of(
                operation != null ? AppointmentType.OPERATION.name() : null,
                medication != null ? AppointmentType.MEDICATION.name() : null,
                procedure != null ? AppointmentType.PROCEDURE.name() : null
        ).collect(Collectors.toList());
        if (status != null) {
            types.add(AppointmentStatus.valueOf(status).name());
        }
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        try {
            int offset = Math.max((pageNo - 1) * recordsPerPage, 0);
            List<Appointment> appointments = appointmentService.getAppointmentsFiltered(types, offset, recordsPerPage);
            session.setAttribute("appointments", appointments);
            int noOfRecords = appointmentService.getNumberOfRecords();
            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPageNo", pageNo);
            logger.debug("Number of pages {}", noOfPages);
            session.setAttribute("action", "appointments");
        } catch (EntityNotFoundException | UnknownSqlException e) {
            logger.error("Error - {}",  e.getMessage());
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult("/doctorListAppointments.jsp");
    }
}
