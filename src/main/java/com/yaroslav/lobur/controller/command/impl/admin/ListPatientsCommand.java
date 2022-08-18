package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListPatientsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ListPatientsCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        String sortBy = request.getParameter("sorting_type");
        OrderBy order = sortBy == null ? OrderBy.NAME : OrderBy.valueOf(sortBy);
        session.setAttribute("sortBy", order.name());
        logger.info("Sorting patients by {}", order);
        session.setAttribute("patients", patientService.getAllPatientsSorted(order));
        String page = PagePathManager.getProperty("page.admin.patients");
        session.setAttribute("currentPage", page);
        return new CommandResult(page);
    }
}
