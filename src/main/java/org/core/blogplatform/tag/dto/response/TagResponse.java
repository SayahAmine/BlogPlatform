package org.core.blogplatform.tag.dto.response;

import java.util.UUID;

public record TagResponse(
        UUID id,
        String name,
        Integer postCount
) {
}
