package de.ghse.forum.api;

import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.request.RegisterRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication related endpoints.
 *
 * @apiNote This controller is accessible under /api/v1/auth without an Authentication Header.
 * @see AuthenticationService
 * @see de.ghse.forum.config.JwtAuthenticationFilter JwtAuthenticationFilter
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  /**
   * REST endpoint for registering a new user using NoeAuth.
   *
   * @apiNote This endpoint is accessible under /api/v1/auth/register without an Authentication
   *     Header.
   * @param request the RegisterRequest containing the user_name and password
   * @return the AuthenticationResponse containing the JWT token and the user id
   * @see de.ghse.forum.api.request.RegisterRequest RegisterRequest
   * @see de.ghse.forum.api.response.AuthenticationResponse AuthenticationResponse
   */
  @PostMapping(path = "/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    logger.info("NoeAuth User: " + request.getUser_name());
    try {
      return ResponseEntity.ok(authenticationService.noeAuth(request));
    } catch (Exception e) {
      logger.error("NoeAuth Error: " + e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * REST endpoint for authenticating a user.
   *
   * @apiNote This endpoint is accessible under /api/v1/auth/authenticate without an Authentication
   *     Header.
   * @param request the AuthenticationRequest containing the user_name and password
   * @return the AuthenticationResponse containing the JWT token and the user id
   * @see de.ghse.forum.api.request.AuthenticationRequest AuthenticationRequest
   * @see de.ghse.forum.api.response.AuthenticationResponse AuthenticationResponse
   */
  @PostMapping(path = "/login")
  public ResponseEntity<AuthenticationResponse> authentication(
      @RequestBody AuthenticationRequest request) {
    logger.info("Authenticating user with username: " + request.getUser_name());
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  /**
   * REST endpoint for testing the Application.
   *
   * @apiNote This endpoint is accessible under /api/v1/auth/test without an Authentication Header.
   * @return the String "test"
   * @deprecated This endpoint is only for testing purposes and will be removed in the future.
   */
  @GetMapping(path = "/test")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("test");
  }
}
