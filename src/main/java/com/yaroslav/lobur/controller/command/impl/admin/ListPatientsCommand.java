package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListPatientsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ListPatientsCommand.class);

    private static final int NUMBER_OF_RECORDS_PER_PAGE = 5;

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
        String sortBy = request.getParameter("sorting_type");
        OrderBy order = sortBy == null ? OrderBy.NAME : OrderBy.valueOf(sortBy);

        int pageNo = 1;
        if (request.getParameter("page") != null)
            pageNo = NumberUtils.toInt(request.getParameter("page"));

        int recordsPerPage = NumberUtils.toInt(request.getParameter("recordsPerPage"));
        recordsPerPage = recordsPerPage == 0 ? NUMBER_OF_RECORDS_PER_PAGE : recordsPerPage;

        request.setAttribute("recordsPerPage", recordsPerPage);

        session.setAttribute("sortBy", order.name());
        logger.info("Sorting patients by {}", order);
        try {
            int offset = Math.max((pageNo - 1) * recordsPerPage, 0);
            logger.debug("offset {}", offset);
            var patients = patientService.getAllPatientsSorted(order, offset, recordsPerPage);
            var categories = doctorService.getAllCategories();
            int noOfRecords = patientService.getNumberOfRecords();
            boolean noDoctor = request.getParameter("no_doctor") != null && request.getParameter("no_doctor").equals("true");
            session.setAttribute("no_doctor", noDoctor);
            if (noDoctor) {
                patients = patientService.getPatientsWithoutDoctor(order, offset, recordsPerPage);
                noOfRecords = patientService.getNumberOfRecords();
            }
            session.setAttribute("categories", categories);
            session.setAttribute("patients", patients);
            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPageNo", pageNo);
        } catch (UnknownSqlException | EntityNotFoundException e) {
            logger.error("Error - {}",  e.getMessage());
            request.setAttribute("sql", "sql.error");
        }
        String page = PagePathManager.getProperty("page.admin.patients");
        session.setAttribute("currentPage", page);
        return new CommandResult(page);
    }
}
