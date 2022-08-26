package com.yaroslav.lobur.controller.command.impl.nurse;

import com.yaroslav.lobur.controller.command.Command;
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

public class NurseAppointmentsCommand implements Command {

    private static final int NUMBER_OF_RECORDS_PER_PAGE = 5;

    private static final Logger logger = LoggerFactory.getLogger(NurseAppointmentsCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String medication = request.getParameter("MEDICATION");
        String procedure = request.getParameter("PROCEDURE");
        String status = request.getParameter("status");

        int pageNo = 1;
        if (request.getParameter("page") != null)
            pageNo = NumberUtils.toInt(request.getParameter("page"));

        int recordsPerPage = NumberUtils.toInt(request.getParameter("recordsPerPage"));
        recordsPerPage = recordsPerPage == 0 ? NUMBER_OF_RECORDS_PER_PAGE : recordsPerPage;
        request.setAttribute("recordsPerPage", recordsPerPage);

        List<String> appointmentTypes = Arrays.asList(
                null,
                medication != null ? AppointmentType.MEDICATION.name() : null,
                procedure != null ? AppointmentType.PROCEDURE.name() : null
        );
        AppointmentStatus appointmentStatus = status != null ? AppointmentStatus.valueOf(status) : null;
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        try {
            int offset = Math.max((pageNo - 1) * recordsPerPage, 0);
            List<Appointment> appointments = appointmentService.getAppointmentsFiltered(appointmentTypes, offset, recordsPerPage);
            if (appointmentStatus != null) {
                appointments = appointments.stream().filter(a -> a.getStatus().name().equals(appointmentStatus.name())).collect(Collectors.toList());
            }
            session.setAttribute("appointments", appointments);
            int noOfRecords = appointmentService.getNumberOfRecords();
            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPageNo", pageNo);
        } catch (Exception e) {
            logger.error("", e);
        }
        return new CommandResult("/nurseAppointments.jsp");
    }
}
