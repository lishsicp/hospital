package com.yaroslav.lobur.controller.command;

import com.yaroslav.lobur.controller.command.impl.*;
import com.yaroslav.lobur.controller.command.impl.admin.ListDoctorsCommand;
import com.yaroslav.lobur.controller.command.impl.admin.ListPatientsCommand;
import com.yaroslav.lobur.controller.command.impl.admin.AdminAddPatientCommand;
import com.yaroslav.lobur.controller.command.impl.authentication.LogoutCommand;
import com.yaroslav.lobur.controller.command.impl.authentication.UserSignInCommand;
import com.yaroslav.lobur.controller.command.impl.authentication.UserSignUpCommand;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    private static volatile CommandHandler commandHandler;

    private final Map<String, Command> commandMap = new HashMap<>();

    private CommandHandler() {
        commandMap.put("sign_in", new UserSignInCommand());
        commandMap.put("sign_up", new UserSignUpCommand());
        commandMap.put("unknown_command", new UnknownCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("view_user", new ViewUserCommand());
        commandMap.put("list_patients", new ListPatientsCommand());
        commandMap.put("list_doctors", new ListDoctorsCommand());
        commandMap.put("add_patient", new AdminAddPatientCommand());
    }

    public static CommandHandler getInstance() {
        CommandHandler localInstance = commandHandler;
        if (localInstance == null) {
            synchronized (CommandHandler.class) {
                localInstance = commandHandler;
                if (localInstance == null) {
                    commandHandler = localInstance = new CommandHandler();
                }
            }
        }
        return localInstance;
    }

    public Command defineCommand(HttpServletRequest req) {
        String action = req.getParameter("action");
        logger.debug("Attempting to make an action: {}", action);
        if (action == null || action.isEmpty()) {
            logger.info("Empty or unknown command {} {}", req.getMethod(), req.getRequestURI());
            req.setAttribute("command", action);
            action = "unknown_command";
        } else {
            logger.info("Command: {} {}", req.getMethod(), action);
        }
        return commandMap.getOrDefault(action, new UnknownCommand());
    }


}
