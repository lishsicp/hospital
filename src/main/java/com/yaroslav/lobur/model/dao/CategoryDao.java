package com.yaroslav.lobur.model.dao;

import com.yaroslav.lobur.model.entity.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAllCategories();
    Category findCategoryById(long id);
}
