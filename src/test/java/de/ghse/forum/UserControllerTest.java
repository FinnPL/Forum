package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.api.response.UserResponse;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import de.ghse.forum.service.JwtService;

import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
  @Autowired private UserRepository userRepository;
  @Autowired private JwtService jwtService;
  @Autowired private TestRestTemplate restTemplate;

  @Test
  void getUser() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    String id = user.getId().toString();
    String token = jwtService.generateToken(user);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<UserResponse> response =
        restTemplate.exchange("/api/v1/user/" + id, HttpMethod.GET, entity, UserResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody()).getUser_name()).isEqualTo(user.getUsername());
    userRepository.delete(user);
  }

  @Test
  void getUserWithWrongId() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    String id = UUID.randomUUID().toString();
    String token = jwtService.generateToken(user);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<UserResponse> response =
        restTemplate.exchange("/api/v1/user/" + id, HttpMethod.GET, entity, UserResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    userRepository.delete(user);
  }
}
