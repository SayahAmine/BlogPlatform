package org.core.blogplatform.user.service;


import lombok.RequiredArgsConstructor;
import org.core.blogplatform.security.JwtService;
import org.core.blogplatform.security.CustomUserDetails;
import org.core.blogplatform.user.domain.User;
import org.core.blogplatform.user.dto.mapper.UserMapper;
import org.core.blogplatform.user.dto.request.LoginRequest;
import org.core.blogplatform.user.dto.request.RegisterRequest;
import org.core.blogplatform.user.dto.response.LoginResponse;
import org.core.blogplatform.user.dto.response.UserResponse;
import org.core.blogplatform.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered: " + request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        //Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        //Fetch the user
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //Generate JWT
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtService.generateToken(user, userDetails);

        //Return token + user info
        return new LoginResponse(jwtToken, userMapper.toResponse(user));
    }
}