package com.yaroslav.lobur.utils.requestparsers;

import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.Role;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;

public class UserRequestParser {
    public static User parseUser(HttpServletRequest request) {
        User user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("psw"));
        user.setFirstname(request.getParameter("firstName"));
        user.setLastname(request.getParameter("lastName"));
        user.setDateOfBirth(Date.valueOf(request.getParameter("date_of_birth")));
        user.setGender(request.getParameter("gender"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setLocale(Locale.UK);
        user.setAddress(request.getParameter("address"));
        // default role
        user.setRole(Role.DOCTOR);
        return user;
    }
}
