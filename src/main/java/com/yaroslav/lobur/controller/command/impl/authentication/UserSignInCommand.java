package com.yaroslav.lobur.controller.command.impl.authentication;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.exceptions.EntityNotFoundException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.service.UserService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.utils.VerifyReCaptchaUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSignInCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(UserSignInCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.sign_in");
        String email = request.getParameter("email");
        String password = request.getParameter("psw");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");

        boolean verify;

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        logger.info("gRecaptchaResponse= {}", gRecaptchaResponse);

        // Verify CAPTCHA.
        verify = VerifyReCaptchaUtil.verify(gRecaptchaResponse);
        if (!verify) {
            request.setAttribute("captchaError", "captcha.error");
            return new CommandResult(page);
        }
        try {
            long id = userService.signIn(email, password);
            User user = userService.getUserById(id);
            session.setAttribute("current_user", user);
            logger.info("User successfully logged in as {}", user.getRole());
            session.setAttribute("viewUser", user);
            if (user.getRole().getRoleId() == 1) {
                page = PagePathManager.getProperty("page.admin.patients");
                return new CommandResult(request.getRequestURI() + "?action=list_patients", true);
            }
            if (user.getRole().getRoleId() == 2) {
                DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
                session.setAttribute("currentDoctor", doctorService.getDoctorByUser(user));
                return new CommandResult(request.getRequestURI() + "?action=my_patients", true);
            }
            if (user.getRole().getRoleId() == 3) {
                return new CommandResult(request.getRequestURI() + "?action=nurse_appointments", true);
            }
            return new CommandResult(request.getRequestURI() + "?action=view_user&user_id=" + id, true);
        } catch (DBExceptionMessages e) {
            logger.debug("{}", e.getErrorMessages());
            logger.debug("Login failed. Incorrect login or password.");
            session.setAttribute("errors", e.getErrorMessages());
        } catch (UnknownSqlException | EntityNotFoundException e) {
            logger.error("", e);
            request.setAttribute("sql", "sql.error");
        }
        return new CommandResult(page);
    }
}
