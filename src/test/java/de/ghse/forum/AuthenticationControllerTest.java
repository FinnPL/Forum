package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void register() {
    AuthenticationRequest authenticationRequest =
        AuthenticationRequest.builder()
            .user_name("Shaen")
            .password(passwordEncoder.encode("sC8Xzltaorcozjky"))
            .build();

    ResponseEntity<AuthenticationResponse> response =
        restTemplate.postForEntity(
            "/api/v1/auth/register", authenticationRequest, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody()).getUser_id()).isNotNull();
    assertThat(response.getBody().getToken()).isNotNull();
    userRepository.delete(userRepository.findByUsername("Shaen").get());
  }

  @Test
  void login() {
    String password = "c5Urg26VKO";
    User user =
        User.builder()
            .username("Ginny")
            .password(passwordEncoder.encode(password))
            .role(Role.USER)
            .build();
    userRepository.save(user);

    AuthenticationRequest authenticationRequest =
        AuthenticationRequest.builder().user_name(user.getUsername()).password(password).build();

    ResponseEntity<AuthenticationResponse> response =
        restTemplate.postForEntity(
            "/api/v1/auth/login", authenticationRequest, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody()).getUser_id()).isNotNull();
    assertThat(response.getBody().getToken()).isNotNull();
    userRepository.delete(user);
  }
}
