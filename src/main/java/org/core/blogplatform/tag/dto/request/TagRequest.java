package org.core.blogplatform.tag.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record TagRequest(
        @NotEmpty(message = "At least one tag name is required")
        @Size(max = 10, message = "Maximum {max} tags allowed")
        Set<
                @Size(min = 2, max = 30, message = "Tag name must be between {min} and {max} characters")
                @Pattern(regexp = "^[\\w\\s-]+$", message = "Tag name can only contain letters, numbers, spaces, and hyphens")
                        String> names
) {
}