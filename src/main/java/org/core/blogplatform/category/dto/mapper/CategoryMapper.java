package org.core.blogplatform.category.dto.mapper;

import org.core.blogplatform.category.domain.Category;
import org.core.blogplatform.category.dto.request.CategoryRequest;
import org.core.blogplatform.category.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;


@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .name(request.name())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        long postCount = (category.getPosts() == null) ? 0L : category.getPosts().size();

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                postCount
        );
    }
}