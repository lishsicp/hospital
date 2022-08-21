package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.service.DoctorService;
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
        DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
        String sortBy = request.getParameter("sorting_type");
        OrderBy order = sortBy == null ? OrderBy.NAME : OrderBy.valueOf(sortBy);
        session.setAttribute("sortBy", order.name());
        logger.info("Sorting patients by {}", order);
        try {
            var patients = patientService.getAllPatientsSorted(order);
            var doctors = doctorService.getAllDoctorsOrderBy(OrderBy.NAME, 0, 100);
            patients.stream()
                    .filter(p -> p.getDoctor() != null)
                    .forEach(p -> p.setDoctor(doctors
                            .stream()
                            .filter(d -> d.getId() == p.getDoctor().getId())
                            .findFirst()
                            .get()));
            session.setAttribute("patients", patients);
        } catch (UnknownSqlException | EntityNotFoundException e) {
            logger.error("Error - {}",  e.getMessage());
            session.setAttribute("exception", e);
        }
        String page = PagePathManager.getProperty("page.admin.patients");
        session.setAttribute("currentPage", page);
        return new CommandResult(page);
    }
}
