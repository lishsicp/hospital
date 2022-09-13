package com.yaroslav.lobur.controller.command.impl.admin;

import com.yaroslav.lobur.controller.command.Command;
import com.yaroslav.lobur.exceptions.InputErrorsMessagesException;
import com.yaroslav.lobur.exceptions.UnknownSqlException;
import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.service.DoctorService;
import com.yaroslav.lobur.utils.CommandResult;
import com.yaroslav.lobur.utils.managers.PagePathManager;
import com.yaroslav.lobur.validator.CategoryValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AdminAddCategoryCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(AdminAddCategoryCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page = PagePathManager.getProperty("page.admin.addCategory");
        if (request.getMethod().equals("GET"))
            return new CommandResult(page);

        HttpSession session = request.getSession();
        DoctorService doctorService = (DoctorService) request.getServletContext().getAttribute("doctorService");
        Category category = new Category();
        String categoryName = request.getParameter("categoryName");
        category.setName(categoryName);
        var errors = CategoryValidator.getInstance().validate(category);
        if (errors.isEmpty())
            try {
                doctorService.addCategory(category);
            } catch (InputErrorsMessagesException e) {
                logger.debug("Validation fail");
                errors.putAll(e.getErrorMessageMap());
            } catch (UnknownSqlException e) {
                request.setAttribute("sql", "sql.error");
            } catch (Exception e) {
                logger.error("",e);
            }
        if (!errors.isEmpty()) {
            logger.debug("Category is not valid");
            request.setAttribute("categoryErrors", errors);
            return new CommandResult(page);
        } else {
            session.setAttribute("success", "category.success");
        }
        return new CommandResult(request.getContextPath() + page, true);
    }
}
