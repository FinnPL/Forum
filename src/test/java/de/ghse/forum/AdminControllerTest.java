package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import de.ghse.forum.repository.UserRepository;
import de.ghse.forum.service.JwtService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private JwtService jwtService;
  @Autowired private PostRepository postRepository;
  private User user;
  private User adminUser;

  @BeforeEach
  void setUp() {
    user = User.builder().username("Daria").password("R0igSkXKSYQ49vyhiw").role(Role.USER).build();
    userRepository.save(user);

    adminUser =
        User.builder()
            .username("Keishawn")
            .password("hJLgx5emcAp3Gl7qrp5K1fIP")
            .role(Role.ADMIN)
            .build();
    userRepository.save(adminUser);
  }

  @Test
  void deletePostAsAdmin() {
    Post post =
        Post.builder()
            .title("designs")
            .content(
                "Advocacy alternatively swaziland brokers charter peterson honor, movement fellow"
                    + " another toolkit protection strong flashers.")
            .user(user)
            .build();
    postRepository.save(post);
    String id = post.getId().toString();
    String token = jwtService.generateToken(adminUser);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange("/api/v1/admin/post/" + id, HttpMethod.DELETE, entity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(postRepository.findById(UUID.fromString(id)).isPresent()).isFalse();
    userRepository.delete(user);
    userRepository.delete(adminUser);
  }

  @Test
  void deleteUserAsAdmin() {
    String id = user.getId().toString();
    String token = jwtService.generateToken(adminUser);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange("/api/v1/admin/user/" + id, HttpMethod.DELETE, entity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(userRepository.findById(UUID.fromString(id)).isPresent()).isFalse();
    userRepository.delete(adminUser);
  }
}
