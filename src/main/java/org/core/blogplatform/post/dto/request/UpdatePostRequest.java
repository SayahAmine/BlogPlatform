package org.core.blogplatform.post.dto.request;

import jakarta.validation.constraints.Size;
import org.core.blogplatform.post.domain.PostStatus;

import java.util.Set;
import java.util.UUID;

public record UpdatePostRequest(
        @Size(min = 3, max = 200, message = "Title must be between {min} and {max} characters")
        String title,

        @Size(min = 10, max = 50000, message = "Content must be between {min} and {max} characters")
        String content,

        UUID categoryId,

        @Size(max = 10, message = "Maximum {max} tags allowed")
        Set<UUID> tagIds,

        PostStatus status
) {}