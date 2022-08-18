package com.yaroslav.lobur.controller.command;

import com.yaroslav.lobur.utils.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    CommandResult execute(HttpServletRequest request, HttpServletResponse response);
}
