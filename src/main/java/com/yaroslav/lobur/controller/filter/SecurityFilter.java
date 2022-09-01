package com.yaroslav.lobur.controller.filter;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Role;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {

    private Map<Role, Set<String>> urlMap;

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
        urlMap = (Map<Role, Set<String>>) config.getServletContext().getAttribute("securityMap");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getServletPath();
        String command = httpRequest.getParameter("action");
        logger.info("Servlet path {}", path);
        if (path.endsWith("hospital/")
                || path.endsWith("sign_in.jsp")
                || path.endsWith("css")
                || path.endsWith("js")
                || path.endsWith("pdf")
                || command != null && command.equals("sign_in")) {
            logger.info("skipping filter");
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpRequest.getSession();
        User currentUser = (User) session.getAttribute("current_user");
        if (currentUser == null) {
            logger.info("forward to sign in");
            httpRequest.getRequestDispatcher(PagePathManager.getProperty("page.sign_in")).forward(request, response);
            return;
        }

        if ((!urlMap.get(Role.ADMIN).contains(path) && !urlMap.get(Role.DOCTOR).contains(path) && !urlMap.get(Role.NURSE).contains(path)) && command == null) {
            httpRequest.getRequestDispatcher("/error404.jsp").forward(request, response);
            return;
        }
        if (command != null && (!urlMap.get(Role.ADMIN).contains(command) && !urlMap.get(Role.DOCTOR).contains(command) && !urlMap.get(Role.NURSE).contains(command))) {
            httpRequest.getRequestDispatcher("/errorUnknownCommand.jsp").forward(request, response);
            return;
        }
        if (!urlMap.get(currentUser.getRole()).contains(path) && !urlMap.get(currentUser.getRole()).contains(command)) {
            logger.info("Unauthorized access request");
            httpRequest.getRequestDispatcher("/accessError.jsp").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
