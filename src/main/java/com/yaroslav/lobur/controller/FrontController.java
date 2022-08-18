package com.yaroslav.lobur.controller;

import com.yaroslav.lobur.controller.command.CommandHandler;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.yaroslav.lobur.controller.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@WebServlet(name="Controller", urlPatterns = "/controller")
public class FrontController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandHandler.getInstance().defineCommand(request);
        CommandResult result = command.execute(request, response);
        String page = result.getUrl();
        if (result.isRedirect()) {
            logger.info("Redirect to {}", page);
            response.sendRedirect(page);
        } else {
            logger.info("Forward to {}", page);
            getServletContext().getRequestDispatcher(page).forward(request, response);
        }
    }
}
