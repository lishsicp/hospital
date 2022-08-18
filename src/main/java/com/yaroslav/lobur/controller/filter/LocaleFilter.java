package com.yaroslav.lobur.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "LocaleFilter", urlPatterns = "/*")
public class LocaleFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(LocaleFilter.class);

    private static final String LANGUAGE = "language";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String language = req.getParameter(LANGUAGE);
        if (language != null) {
            session.setAttribute(LANGUAGE, language);
            logger.debug("Setting language to {}", language);
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        if (session.getAttribute(LANGUAGE) == null) {
            language = String.valueOf(req.getLocale());
            session.setAttribute(LANGUAGE, language);
        }
        chain.doFilter(request, response);
    }
}
