package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ListDoctorsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ListDoctorsCommand.class);

    private static final int NUMBER_OF_RECORDS_PER_PAGE = 5;

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");

        String sortBy = request.getParameter("sorting_type");
        OrderBy order = sortBy == null ? OrderBy.NAME : OrderBy.valueOf(sortBy);

        int pageNo = 1;
        if (request.getParameter("page") != null)
            pageNo = NumberUtils.toInt(request.getParameter("page"));

        int recordsPerPage = NumberUtils.toInt(request.getParameter("recordsPerPage"));
        recordsPerPage = recordsPerPage == 0 ? NUMBER_OF_RECORDS_PER_PAGE : recordsPerPage;

        session.setAttribute("recordsPerPage", recordsPerPage);
        session.setAttribute("sortBy", order.name());

        try {
            List<Doctor> doctors = doctorService.getAllDoctorsOrderBy(order, (pageNo - 1) * recordsPerPage, recordsPerPage);
            int noOfRecords = doctorService.getNumberOfRecords();
            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
            session.setAttribute("doctors", doctors);
            session.setAttribute("noOfPages", noOfPages);
            session.setAttribute("currentPageNo", pageNo);
        } catch (EntityNotFoundException | UnknownSqlException e) {
            logger.error("", e);
        }
        String page = PagePathManager.getProperty("page.admin.doctors");
        session.setAttribute("currentPage", page);
        return new CommandResult(page);
    }
}
