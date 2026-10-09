package org.core.blogplatform.tag.dto.mapper;

import org.core.blogplatform.tag.domain.Tag;
import org.core.blogplatform.tag.dto.request.TagRequest;
import org.core.blogplatform.tag.dto.response.TagResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public Tag toEntity(String name) {
        if (name == null) {
            return null;
        }
        return Tag.builder()
                .name(name)
                .build();
    }

    public List<Tag> toEntities(TagRequest request) {
        if (request == null || request.names() == null ) {
            return Collections.emptyList();
        }

        return request.names().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

    }

    public TagResponse toResponse(Tag tag) {
        if (tag == null) {
            return null;
        }

        // Null safe calculation of postCount
        int postCount = (tag.getPosts() == null) ? 0 : tag.getPosts().size();

        return new TagResponse(
                tag.getId(),
                tag.getName(),
                postCount
        );
    }
}
