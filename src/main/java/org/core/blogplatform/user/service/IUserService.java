package org.core.blogplatform.user.service;

import org.core.blogplatform.user.domain.User;
import org.core.blogplatform.user.dto.request.LoginRequest;
import org.core.blogplatform.user.dto.request.RegisterRequest;
import org.core.blogplatform.user.dto.response.LoginResponse;
import org.core.blogplatform.user.dto.response.UserResponse;

import java.util.UUID;

public interface IUserService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);

}
