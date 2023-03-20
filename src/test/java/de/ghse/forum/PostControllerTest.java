package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.api.request.PostRequest;
import de.ghse.forum.api.response.PostResponse;
import de.ghse.forum.model.Post;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
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
class PostControllerTest {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private PostRepository postRepository;
  @Autowired private JwtService jwtService;

  @Test
  void addPost() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);

    String token = jwtService.generateToken(user);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);

    PostRequest postRequest =
        PostRequest.builder()
            .title(new Faker().book().title())
            .content(new Faker().yoda().quote().substring(0, 20))
            .build();

    ResponseEntity<PostResponse> response =
        restTemplate.exchange(
            "/api/v1/post/add",
            HttpMethod.POST,
            new HttpEntity<>(postRequest, headers),
            PostResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(Objects.requireNonNull(response.getBody()).getTitle())
        .isEqualTo(postRequest.getTitle());
    assertThat(response.getBody().getContent()).isEqualTo(postRequest.getContent());

    postRepository.delete(
        postRepository.findById(UUID.fromString(response.getBody().getId())).orElseThrow());
    userRepository.delete(user);
  }

  @Test
  void deletePost() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    Post post =
        Post.builder()
            .title(new Faker().book().title())
            .content(new Faker().yoda().quote().substring(0, 20))
            .user(user)
            .build();
    postRepository.save(post);

    String token = jwtService.generateToken(user);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);

    ResponseEntity<PostResponse> response =
        restTemplate.exchange(
            "/api/v1/post/del/" + post.getId(),
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            PostResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    userRepository.delete(user);
  }

  @Test
  void updatePost() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    Post post =
        Post.builder()
            .title(new Faker().book().title())
            .content(new Faker().yoda().quote())
            .user(user)
            .build();
    postRepository.save(post);
    post.setTitle(new Faker().book().title());
    post.setContent(new Faker().yoda().quote().substring(0, 20));

    String token = jwtService.generateToken(user);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);

    ResponseEntity<PostResponse> response =
        restTemplate.exchange(
            "/api/v1/post/" + post.getId(),
            HttpMethod.PUT,
            new HttpEntity<>(post, headers),
            PostResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody()).getTitle()).isEqualTo(post.getTitle());
    assertThat(response.getBody().getContent()).isEqualTo(post.getContent());

    postRepository.delete(post);
    userRepository.delete(user);
  }
}
