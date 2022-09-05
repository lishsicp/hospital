package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.User;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.utils.PasswordEncryptor;
import com.yaroslav.lobur.utils.requestparsers.UserRequestParser;
import com.yaroslav.lobur.validator.DoctorValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        User user = UserRequestParser.parseUser(request);
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
        if (errors.isEmpty())
            try {
                user.setPassword(PasswordEncryptor.getSHA1String(user.getPassword()));
                doctorService.addDoctor(doctor);
            } catch (InputErrorsMessagesException e) {
                logger.debug("Validation fail");
                errors.putAll(e.getErrorMessageMap());
            } catch (UnknownSqlException e) {
                request.setAttribute("sql", "sql.error");
            }
        if (!errors.isEmpty()) {
            logger.debug("Doctor is not valid");
            request.setAttribute("doctorErrors", errors);
            return new CommandResult(page);
        } else {
            session.setAttribute("success", "signup.doctor.success");
        }
        return new CommandResult(request.getContextPath() + page, true);
    }
}
