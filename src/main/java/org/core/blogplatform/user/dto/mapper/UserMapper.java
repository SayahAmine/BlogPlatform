package org.core.blogplatform.user.dto.mapper;

import org.core.blogplatform.user.domain.User;
import org.core.blogplatform.user.dto.request.RegisterRequest;
import org.core.blogplatform.user.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password()) // Password hashing happens in the service
                .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreateAt(),
                user.getUpdateAt()
        );
    }
}
