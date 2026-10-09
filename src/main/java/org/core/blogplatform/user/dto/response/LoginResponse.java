package org.core.blogplatform.user.dto.response;

public record LoginResponse(
        String token,
        UserResponse user
) {}