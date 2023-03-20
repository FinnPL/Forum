package de.ghse.forum.api;

import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.request.RegisterRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.service.AuthenticationService;
import de.ghse.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  private final UserService userService;
  final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  @PostMapping(path = "/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    if (userService.findbyUsername(request.getUser_name()).isPresent()) {
      logger.error("User with username: " + request.getUser_name() + " already exists");
      return ResponseEntity.badRequest().build();
    }
    logger.info("Registering user with username: " + request.getUser_name());
    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping(path = "/authenticate")
  public ResponseEntity<AuthenticationResponse> authentication(
      @RequestBody AuthenticationRequest request) {
    logger.info("Authenticating user with username: " + request.getUser_name());
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
  @GetMapping(path = "/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
