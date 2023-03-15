package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.request.PostRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {
  private static String username;
  private static String password;

  private String token;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  public static void setUp() {
    Faker faker = new Faker();
    username = faker.name().username();
    password = faker.internet().password();
  }

  private void login() {
    ResponseEntity<AuthenticationResponse> response =
        restTemplate.postForEntity(
            "/api/v1/auth/register",
            AuthenticationRequest.builder().user_name(username).password(password).build(),
            AuthenticationResponse.class);
    token = response.getBody().getToken();
  }

  @Test
  public void addPost() {
    if (token == null) login();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);

    PostRequest postRequest =
        PostRequest.builder()
            .title(new Faker().book().title())
            .content(new Faker().yoda().quote())
            .build();

    ResponseEntity<String> response =
        restTemplate.exchange(
            "/api/v1/post/add",
            HttpMethod.POST,
            new HttpEntity<>(postRequest, headers),
            String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }
}
