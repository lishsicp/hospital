package com.yaroslav.lobur.controller.command.impl.authentication;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogoutCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(LogoutCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        logger.trace("Session was invalidated");
        session.invalidate();
        return new CommandResult(request.getContextPath() + PagePathManager.getProperty("page.sign_in"), true);
    }
}
