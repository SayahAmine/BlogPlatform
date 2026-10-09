package org.core.blogplatform.user.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        LocalDate createAt,
        LocalDate updateAt
) {
}
