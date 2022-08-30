package com.yaroslav.lobur.controller.command;

import com.yaroslav.lobur.controller.command.impl.*;
import com.yaroslav.lobur.controller.command.impl.admin.*;
import com.yaroslav.lobur.controller.command.impl.authentication.LogoutCommand;
import com.yaroslav.lobur.controller.command.impl.authentication.UserSignInCommand;
import com.yaroslav.lobur.controller.command.impl.doctor.*;
import com.yaroslav.lobur.controller.command.impl.nurse.NurseAppointmentsCommand;
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
        commandMap.put("unknown_command", new UnknownCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("view_user", new ViewUserCommand());
        commandMap.put("list_patients", new ListPatientsCommand());
        commandMap.put("list_doctors", new ListDoctorsCommand());
        commandMap.put("add_patient", new AdminAddPatientCommand());
        commandMap.put("add_doctor", new AdminAddDoctorCommand());
        commandMap.put("delete_patient", new DeletePatientCommand());
        commandMap.put("edit_patient", new EditPatientCommand());
        commandMap.put("doctors_by_category", new DoctorsByCategory());
        commandMap.put("assign_doctor", new AssignDoctorCommand());
        commandMap.put("my_patients", new MyPatientsCommand());
        commandMap.put("view_patient", new ViewPatientCommand());
        commandMap.put("update_diagnosis", new UpdatePatientDiagnosisCommand());
        commandMap.put("assign_appointment", new DoctorAssignAppointmentCommand());
        commandMap.put("update_appointment", new UpdateAppointmentCommand());
        commandMap.put("appointments", new ListAppointmentsCommand());
        commandMap.put("nurse_appointments", new NurseAppointmentsCommand());
        commandMap.put("make_appointment", new MakeAppointmentCommand());
        commandMap.put("discharge_patient", new DischargePatientCommand());
        commandMap.put("download", new DownloadDischargeInfo());
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
        if (action == null || action.isEmpty() || commandMap.get(action) == null) {
            logger.info("Empty or unknown command {} {}", req.getMethod(), req.getRequestURI());
            req.setAttribute("command", action);
            action = "unknown_command";
        } else {
            logger.info("Command: {} {}", req.getMethod(), action);
        }
        return commandMap.getOrDefault(action, new UnknownCommand());
    }


}
