package com.yaroslav.lobur.validator;

import com.yaroslav.lobur.model.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryValidatorTest {

    CategoryValidator categoryValidator;
    Category category;

    @BeforeEach
    void setUp() {
        categoryValidator = CategoryValidator.getInstance();
        category = new Category();
        category.setId(1);
        category.setName("test");
    }

    @Test
    void testGetInstance() {
        assertEquals(categoryValidator, CategoryValidator.getInstance());
    }

    @Test
    void validate() {
        assertEquals(0, categoryValidator.validate(category).size());
        category.setName("123");
        assertEquals(1, categoryValidator.validate(category).size());
    }
}