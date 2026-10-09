package org.core.blogplatform.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.core.blogplatform.category.domain.Category;
import org.core.blogplatform.category.dto.mapper.CategoryMapper;
import org.core.blogplatform.category.dto.request.CategoryRequest;
import org.core.blogplatform.category.dto.response.CategoryResponse;
import org.core.blogplatform.category.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories() {
     var categories = categoryService.listCategories()
             .stream()
             .map(categoryMapper ::toResponse)
             .toList();
     return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(createCategoryRequest));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
