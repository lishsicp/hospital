package com.yaroslav.lobur.controller.command.impl;

import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.yaroslav.lobur.controller.command.Command;

public class UnknownCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        return new CommandResult(PagePathManager.getProperty("page.unknown_command"));
    }
}
