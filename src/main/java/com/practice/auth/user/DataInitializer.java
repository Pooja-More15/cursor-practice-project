package com.practice.auth.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    if (userRepository.existsByUsername("demo")) {
      return;
    }
    AppUser demo = new AppUser();
    demo.setUsername("demo");
    demo.setPasswordHash(passwordEncoder.encode("password123"));
    demo.setRole("USER");
    userRepository.save(demo);
  }
}
