package com.practice.auth.auth;

import com.practice.auth.auth.dto.AuthResponse;
import com.practice.auth.auth.dto.LoginRequest;
import com.practice.auth.auth.dto.RegisterRequest;
import com.practice.auth.auth.dto.UserResponse;
import com.practice.auth.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;

  public AuthController(AuthService authService, UserRepository userRepository) {
    this.authService = authService;
    this.userRepository = userRepository;
  }

  @PostMapping("/auth/login")
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }

  @PostMapping("/auth/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @GetMapping("/me")
  public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
    return userRepository
        .findByUsername(jwt.getSubject())
        .map(user -> new UserResponse(user.getUsername(), user.getRole()))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }
}
