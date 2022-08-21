package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.model.entity.enums.Gender;
import com.yaroslav.lobur.model.entity.enums.Locale;
import com.yaroslav.lobur.model.entity.enums.Role;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.service.PatientService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.PagePathManager;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import com.yaroslav.lobur.validator.DoctorValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

public class AdminAddDoctorCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(AdminAddDoctorCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String page = PagePathManager.getProperty("page.admin.addDoctor");
        DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
        if (request.getMethod().equals("GET")) {
            List<Category> categories = doctorService.getAllCategories();
            categories.sort(Comparator.comparingLong(Category::getId));
            session.setAttribute("categories", categories);
            return new CommandResult(page);
        }
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

        Category category = new Category();
        long categoryId = NumberUtils.toLong(request.getParameter("category"));
        category.setId(categoryId);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setCategory(category);

        var errors = DoctorValidator.getInstance().validate(doctor);
        if (!request.getParameter("psw-repeat").equals(user.getPassword())) {
            errors.put("psw-repeat", "validation.user.password_retype");
        }
        if (errors.isEmpty()) {
            try {
                user.setPassword(PasswordEncryptor.getSHA1String(user.getPassword()));
                doctorService.addDoctor(doctor);
                session.removeAttribute("userErrors");
            } catch (InputErrorsMessagesException e) {
                logger.debug("Validation fail");
                errors.putAll(e.getErrorMessageMap());
            }
        }
        if (!errors.isEmpty()) {
            logger.debug("Doctor is not valid {}", category.getId());
            session.setAttribute("doctorErrors", errors);
        } else {
            session.setAttribute("success", "signup.doctor.success");
        }
        return new CommandResult(request.getHeader("referer"), true);
    }
}
