package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.utils.PatientDischargeInfo;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class DownloadDischargeInfo implements Command {

    private static final Logger logger = Logger.getLogger(DownloadDischargeInfo.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        long hospitalCardId = NumberUtils.toLong(request.getParameter("hospitalCardId"));
        try {
            List<Appointment> appointments = appointmentService.getAppointmentByHospitalCardId(hospitalCardId);
            PatientDischargeInfo.createDischargeInfo(appointments, response);
            response.setContentType("application/pdf");
        } catch (EntityNotFoundException e) {
            request.setAttribute("sql", e.getMessage());
            return new CommandResult(request.getHeader("referer"));
        } catch (UnknownSqlException e) {
            request.setAttribute("sql", "sql.error");
        } catch (IOException e) {
            logger.error(e);
            request.setAttribute("dischargeInfo", "discharge.info.error");
        }
        return new CommandResult("/viewPatient.jsp");
    }

}
