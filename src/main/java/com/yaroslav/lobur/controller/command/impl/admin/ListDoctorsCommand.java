package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.model.entity.enums.OrderBy;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ListDoctorsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ListDoctorsCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
        String sortBy = request.getParameter("sorting_type");
        OrderBy order = sortBy == null ? OrderBy.NAME : OrderBy.valueOf(sortBy);
        session.setAttribute("sortBy", order.name());
//        List<Doctor> doctors = doctorService.getAllDoctors();
//        switch (order) {
//            case NAME:
//                doctors.sort((d1, d2) -> d1.getUser().getLastname().compareToIgnoreCase(d2.getUser().getLastname()));
//                break;
//            case CATEGORY:
//                doctors.sort((d1, d2) -> d1.getCategory().getName().compareToIgnoreCase(d2.getCategory().getName()));
//                break;
//            case NUMBER_OF_PATIENTS:
//                doctors.sort(Comparator.comparingLong(Doctor::getNumberOfPatients).reversed());
//                break;
//            default:
//                throw new IllegalArgumentException();
//        }
        session.setAttribute("doctors", doctorService.getAllDoctorsOrderBy(order));
        String page = PagePathManager.getProperty("page.admin.doctors");
        session.setAttribute("currentPage", page);
        return new CommandResult(page);
    }
}
