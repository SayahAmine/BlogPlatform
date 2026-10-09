package org.core.blogplatform.post.dto.response;

import org.core.blogplatform.post.domain.PostStatus;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String title,
        String content,
        PostStatus status,
        Integer readingTime,
        AuthorSummary author,
        CategorySummary category,
        Set<TagSummary> tags,
        LocalDate createAt,
        LocalDate updateAt
) {
    public record AuthorSummary(UUID id, String name) {}
    public record CategorySummary(UUID id, String name) {}
    public record TagSummary(UUID id, String name) {}
}