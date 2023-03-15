package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {
  private static String username;
  private static String password;

  @BeforeAll
  public static void setUp() {
    Faker faker = new Faker();
    username = faker.name().username();
    password = faker.internet().password();
  }

  @Autowired private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  public void testRegisterAndAuth() {
    // create a login request object with valid credentials
    AuthenticationRequest loginRequest = new AuthenticationRequest();
    loginRequest.setUser_name(username);
    loginRequest.setPassword(password);

    // send the login request to the /api/auth/login endpoint
    ResponseEntity<AuthenticationResponse> response =
        restTemplate.postForEntity(
            "/api/v1/auth/register", loginRequest, AuthenticationResponse.class);

    // assert that the response status code is 200 OK
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // assert that the response body contains a non-null JWT token
    assertThat(response.getBody().getToken()).isNotNull();

    response =
        restTemplate.postForEntity(
            "/api/v1/auth/authenticate", loginRequest, AuthenticationResponse.class);

    // assert that the response status code is 200 OK
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // assert that the response body contains a non-null JWT token
    assertThat(response.getBody().getToken()).isNotNull();
  }
}
