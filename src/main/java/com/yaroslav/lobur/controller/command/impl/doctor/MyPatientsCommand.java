package com.yaroslav.lobur.controller.command.impl.doctor;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPatientsCommand implements Command {

    private static final int NUMBER_OF_RECORDS_PER_PAGE = 5;

    private static final Logger logger = LoggerFactory.getLogger(MyPatientsCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Doctor currentDoctor = (Doctor) session.getAttribute("currentDoctor");
        String page = PagePathManager.getProperty("page.doctor.my_patients");
        PatientService patientService = (PatientService) request.getServletContext().getAttribute("patientService");
        int pageNo = 1;
        if (request.getParameter("page") != null)
            pageNo = NumberUtils.toInt(request.getParameter("page"));

        int recordsPerPage = NumberUtils.toInt(request.getParameter("recordsPerPage"));
        recordsPerPage = recordsPerPage == 0 ? NUMBER_OF_RECORDS_PER_PAGE : recordsPerPage;
        request.setAttribute("recordsPerPage", recordsPerPage);
        try {
            int offset = Math.max((pageNo - 1) * recordsPerPage, 0);
            var hospitalCards = patientService.getAllHospitalCardSorted(currentDoctor.getId(), offset, recordsPerPage);
            int noOfRecords = patientService.getNumberOfRecordsHC();
            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPageNo", pageNo);
            session.setAttribute("hospitalCards", hospitalCards);
        } catch (UnknownSqlException e) {
            logger.error("Error - {}",  e.getMessage());
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult(page);
    }
}
