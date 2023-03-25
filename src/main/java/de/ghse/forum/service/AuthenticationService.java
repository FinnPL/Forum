package de.ghse.forum.service;

import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.request.RegisterRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for authentication.
 *
 * @apiNote This service is used by the AuthenticationController.
 * @see de.ghse.forum.api.AuthenticationController AuthenticationController
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /**
   * Generates a JWT token for the user.
   *
   * @param request AuthenticationRequest
   * @return AuthenticationResponse with JWT token and user id
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUser_name(), request.getPassword()));
    var user = userRepository.findByUsername(request.getUser_name()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .user_id(user.getId().toString())
        .build();
  }

  /**
   * Registers a new user.
   *
   * @param request RegisterRequest
   * @return AuthenticationResponse with JWT token and user id
   */
  public AuthenticationResponse register(RegisterRequest request) {
    var user =
        User.builder()
            .username(request.getUser_name())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
    userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .user_id(user.getId().toString())
        .build();
  }
}
