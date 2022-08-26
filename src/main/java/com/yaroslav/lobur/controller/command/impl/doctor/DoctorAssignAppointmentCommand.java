package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.HospitalCard;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;
import com.yaroslav.lobur.service.AppointmentService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import java.time.LocalDate;

public class DoctorAssignAppointmentCommand implements Command {

    private static final Logger logger = Logger.getLogger(DoctorAssignAppointmentCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.view_patient");
        AppointmentService appointmentService = (AppointmentService) request.getServletContext().getAttribute("appointmentService");
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        try {
            Appointment appointment = new Appointment();
            appointment.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
            appointment.setStatus(AppointmentStatus.ONGOING);
            appointment.setType(AppointmentType.valueOf(request.getParameter("appointment")));
            long hospitalCardId = NumberUtils.toLong(request.getParameter("cardId"));
            HospitalCard hospitalCard = patientService
                    .getHospitalCardById(hospitalCardId);
            Patient patient = patientService.getPatientById(hospitalCard.getPatient().getId());
            hospitalCard.setPatient(patient);
            appointment.setHospitalCard(hospitalCard);
            appointment.setTitle(request.getParameter("description"));
            appointmentService.createAppointment(appointment);
            session.setAttribute("success", "doctor.view_patient.app.success");
            return new CommandResult(request.getContextPath() + "/controller?action=view_patient&cardId="+ hospitalCardId, true);
        } catch (EntityNotFoundException | UnknownSqlException e) {
            logger.error("", e);
            request.setAttribute("sql", e.getMessage());
        } catch (InputErrorsMessagesException e) {
            logger.error("", e);
            request.setAttribute("errors", e.getErrorMessageMap());

        }
        return new CommandResult(page);
    }
}
