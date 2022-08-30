package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;

public class DeletePatientCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        long patientId = NumberUtils.toLong(request.getParameter("patientId"));
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        try {
            patientService.deletePatient(patientId);
        } catch (UnknownSqlException e) {
            request.setAttribute("sql", "sql.error");
            new CommandResult(PagePathManager.getProperty("page.admin.patients"));
        }
        return new CommandResult(request.getHeader("referer"), true);
    }
}
