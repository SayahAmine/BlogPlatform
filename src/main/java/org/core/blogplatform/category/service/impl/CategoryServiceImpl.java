package org.core.blogplatform.category.service.impl;

import lombok.RequiredArgsConstructor;

import org.core.blogplatform.category.domain.Category;
import org.core.blogplatform.category.dto.mapper.CategoryMapper;
import org.core.blogplatform.category.dto.request.CategoryRequest;
import org.core.blogplatform.category.dto.response.CategoryResponse;
import org.core.blogplatform.category.repository.CategoryRepository;
import org.core.blogplatform.category.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllByPostCount();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest createCategoryRequest) {
       if(createCategoryRequest == null || createCategoryRequest.name() == null){
           throw new IllegalArgumentException("Category request and name must not be null");
       }

       if(categoryRepository.existsByNameIgnoreCase(createCategoryRequest.name())){
           throw new IllegalArgumentException("Category name already exists" + createCategoryRequest.name());
       }
       Category category = categoryMapper.toEntity(createCategoryRequest);
       Category savedCategory = categoryRepository.save(category);

       return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public void deleteCategory(UUID id) {
       var categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            if(category.getPosts() != null && !category.getPosts().isEmpty()){
                throw new IllegalArgumentException("Category has posts associated with ");
            }
            categoryRepository.delete(category);
        }else {
            throw new IllegalArgumentException("Category id " + id + " does not exist");
        }
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category id not found!"));
        return categoryMapper.toResponse(category);
    }
}
