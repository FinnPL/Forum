package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  public void register() {
    AuthenticationRequest authenticationRequest =
        AuthenticationRequest.builder()
            .user_name(new Faker().name().username())
            .password(passwordEncoder.encode(new Faker().internet().password()))
            .build();

    ResponseEntity<AuthenticationResponse> response =
        restTemplate.postForEntity(
            "/api/v1/auth/register", authenticationRequest, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getUser_id()).isNotNull();
    assertThat(response.getBody().getToken()).isNotNull();
  }

  @Test
  public void login() {
    String password = new Faker().internet().password();
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(passwordEncoder.encode(password))
            .role(Role.USER)
            .build();
    userRepository.save(user);

    AuthenticationRequest authenticationRequest =
        AuthenticationRequest.builder().user_name(user.getUsername()).password(password).build();

    ResponseEntity<AuthenticationResponse> response =
        restTemplate.postForEntity(
            "/api/v1/auth/authenticate", authenticationRequest, AuthenticationResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getUser_id()).isNotNull();
    assertThat(response.getBody().getToken()).isNotNull();
  }
}
