package com.yaroslav.lobur.controller.command.impl;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.service.UserService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ViewUserCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        //long id = Long.parseLong(request.getParameter("user_id"));
        String page = PagePathManager.getProperty("page.view_user");
        //UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        User user = (User) session.getAttribute("current_user");
        session.setAttribute("viewUser", user);
        session.setAttribute("currentPage", page);
        return new CommandResult(page);
    }
}
