package com.yaroslav.lobur.controller.command.impl;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmptyCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
