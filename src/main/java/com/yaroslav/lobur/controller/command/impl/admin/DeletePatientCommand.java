package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;

public class DeletePatientCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        long patientId = NumberUtils.toLong(request.getParameter("patientId"));
        if (patientId > 0) {
            PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
            patientService.deletePatient(patientId);
        }
        return new CommandResult(request.getHeader("referer"), true);
    }
}
