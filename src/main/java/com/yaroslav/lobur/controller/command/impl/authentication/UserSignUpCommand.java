package com.yaroslav.lobur.controller.command.impl.authentication;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.DBExceptionMessages;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.service.UserService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import com.yaroslav.lobur.utils.requestparsers.UserRequestParser;
import com.yaroslav.lobur.validator.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class UserSignUpCommand implements Command {
    Logger logger = LoggerFactory.getLogger(UserSignUpCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.sign_up");
        session.setAttribute("currentPage", page);
        User user = UserRequestParser.parseUser(request);
        Map<String, String> errors = UserValidator.getInstance().validate(user);
        String repeatPsw = request.getParameter("psw-repeat");
        if (!user.getPassword().equals(repeatPsw)) {
            errors.put("psw-repeat", "validation.user.password_retype");
        }
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        try {
            user.setPassword(PasswordEncryptor.getSHA1String(repeatPsw));
            long id = userService.registerUser(user);
            user.setId(id);
        } catch (DBExceptionMessages e) {
            logger.error("registration error");
            errors.put("db", e.getErrorMessages().toString());
        }
        if (!errors.isEmpty()) {
            session.setAttribute("userErrors", errors);
            logger.debug("User is not valid {}", user);
            return new CommandResult(page);
        }
        session.setAttribute("current_user", user);
        return new CommandResult(request.getRequestURI() + "?action=view_user&user_id=" + user.getId(), true);
    }
}
