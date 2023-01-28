package de.ghse.forum.api;

import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.request.RegisterRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping(path = "/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping(path = "/authenticate")
  public ResponseEntity<AuthenticationResponse> authentication(
      @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}
