package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.model.Post;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import de.ghse.forum.repository.UserRepository;
import de.ghse.forum.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {

      @Autowired
      private TestRestTemplate restTemplate;
      @Autowired private UserRepository userRepository;
      @Autowired private JwtService jwtService;
    @Autowired
    private PostRepository postRepository;

    @Test
      public void deletePostAsAdmin(){
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
          postRepository.save(post);
          String id = post.getId().toString();


          User adminUser =
                  User.builder()
                          .username(new Faker().name().username())
                          .password(new Faker().internet().password())
                          .role(Role.ADMIN)
                          .build();
          userRepository.save(adminUser);
            String token = jwtService.generateToken(adminUser);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange("/api/v1/admin/post/"+id, HttpMethod.DELETE, entity, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(postRepository.findById(UUID.fromString(id)).isPresent()).isFalse();
            userRepository.delete(user);
            userRepository.delete(adminUser);
      }

      @Test
    public void deleteUserAsAdmin(){
        User user =
                User.builder()
                        .username(new Faker().name().username())
                        .password(new Faker().internet().password())
                        .role(Role.USER)
                        .build();
        userRepository.save(user);
        String id = user.getId().toString();

        User adminUser =
                User.builder()
                        .username(new Faker().name().username())
                        .password(new Faker().internet().password())
                        .role(Role.ADMIN)
                        .build();
        userRepository.save(adminUser);

        String token = jwtService.generateToken(adminUser);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/admin/user/"+id, HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.findById(UUID.fromString(id)).isPresent()).isFalse();
        userRepository.delete(adminUser);
      }
}
