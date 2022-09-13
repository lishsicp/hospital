package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Category;
import com.yaroslav.lobur.utils.managers.RegexpManager;

import java.util.HashMap;
import java.util.Map;

public class CategoryValidator implements Validator<Category> {

    private static CategoryValidator instance;

    private CategoryValidator() {
    }

    public static CategoryValidator getInstance() {
        if (instance == null) {
            instance = new CategoryValidator();
        }
        return instance;
    }

    @Override
    public Map<String, String> validate(Category category) {
        Map<String, String> errors = new HashMap<>();
        if (category.getName() == null || category.getName().trim().isEmpty() || !category.getName().matches(RegexpManager.getProperty("doctor.category"))) {
            errors.put("categoryName", "validation.doctor.category.name");
        }
        return errors;
    }
}
