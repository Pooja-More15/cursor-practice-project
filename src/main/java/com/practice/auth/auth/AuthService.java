package com.practice.auth.auth;

import com.practice.auth.auth.dto.AuthResponse;
import com.practice.auth.auth.dto.LoginRequest;
import com.practice.auth.auth.dto.RegisterRequest;
import com.practice.auth.user.AppUser;
import com.practice.auth.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse login(LoginRequest request) {
    AppUser user =
        userRepository
            .findByUsername(request.username())
            .orElseThrow(AuthService::invalidLogin);
    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw invalidLogin();
    }
    return AuthResponse.bearer(jwtService.createToken(user), user.getUsername(), user.getRole());
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
    }
    AppUser user = new AppUser();
    user.setUsername(request.username());
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    user.setRole("USER");
    userRepository.save(user);
    return AuthResponse.bearer(jwtService.createToken(user), user.getUsername(), user.getRole());
  }

  private static ResponseStatusException invalidLogin() {
    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
  }
}
