package org.core.blogplatform.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.core.blogplatform.user.dto.request.LoginRequest;
import org.core.blogplatform.user.dto.request.RegisterRequest;
import org.core.blogplatform.user.dto.response.LoginResponse;
import org.core.blogplatform.user.dto.response.UserResponse;
import org.core.blogplatform.user.repository.UserRepository;
import org.core.blogplatform.user.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }


    //TODO redefine getByEmail method
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        UserResponse user = userRepository.getByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }
}