package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.service.UserService;
import com.yaroslav.lobur.utils.AppointmentDischargeInfo;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;

public class DownloadDischargeInfo implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        PatientService patientService = (PatientService)request.getServletContext().getAttribute("patientService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        long appointmentId = NumberUtils.toLong(request.getParameter("appointmentId"));
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            HospitalCard hospitalCard = patientService.getHospitalCardById(appointment.getHospitalCard().getId());
            Patient patient = patientService.getPatientById(hospitalCard.getPatient().getId());
            hospitalCard.setPatient(patient);
            appointment.setHospitalCard(hospitalCard);
            appointment.setUser(userService.getUserById(appointment.getUser().getId()));
            AppointmentDischargeInfo.createDischargeInfo(appointment, response);
            response.setContentType("application/pdf");
        } catch (EntityNotFoundException e) {
            request.setAttribute("sql", e.getMessage());
            return new CommandResult(request.getHeader("referer"));
        } catch (UnknownSqlException e) {
            request.setAttribute("sql", "sql.error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new CommandResult("viewPatient.jsp");
    }

}
