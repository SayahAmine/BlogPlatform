package org.core.blogplatform.category.service;

import org.core.blogplatform.category.domain.Category;
import org.core.blogplatform.category.dto.request.CategoryRequest;
import org.core.blogplatform.category.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {

    List<Category> listCategories();
    CategoryResponse createCategory(CategoryRequest createCategoryRequest);
    void deleteCategory(UUID id);
    CategoryResponse getCategoryById(UUID id);
}
