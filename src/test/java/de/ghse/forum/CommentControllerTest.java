package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import de.ghse.forum.api.request.CommentRequest;
import de.ghse.forum.model.Comment;
import de.ghse.forum.model.Post;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.CommentRepository;
import de.ghse.forum.repository.PostRepository;
import de.ghse.forum.repository.UserRepository;
import de.ghse.forum.service.JwtService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
  @Autowired private UserRepository userRepository;
  @Autowired private JwtService jwtService;
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private PostRepository postRepository;
  @Autowired private CommentRepository commentRepository;

  @Test
  void addComment() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    String token = jwtService.generateToken(user);

    Post post =
        Post.builder()
            .title(new Faker().book().title())
            .content(new Faker().book().title())
            .user(user)
            .build();
    postRepository.save(post);
    String postId = post.getId().toString();

    CommentRequest commentRequest =
        CommentRequest.builder().content(new Faker().book().title()).post_id(postId).build();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "/api/v1/comment",
            HttpMethod.POST,
            new HttpEntity<>(commentRequest, headers),
            String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(
            commentRepository
                .findCommentByPostByPage(UUID.fromString(postId), Pageable.ofSize(1))
                .get(0)
                .getContent())
        .isEqualTo(commentRequest.getContent());
    commentRepository.delete(
        commentRepository
            .findCommentByPostByPage(UUID.fromString(postId), Pageable.ofSize(1))
            .get(0));
  }

  @Test
  void getComment() {
    User user =
        User.builder()
            .username(new Faker().name().username())
            .password(new Faker().internet().password())
            .role(Role.USER)
            .build();
    userRepository.save(user);
    String token = jwtService.generateToken(user);

    Post post =
        Post.builder()
            .title(new Faker().book().title())
            .content(new Faker().book().title())
            .user(user)
            .build();
    postRepository.save(post);
    String postId = post.getId().toString();

    Comment comment =
        Comment.builder().content(new Faker().book().title()).user(user).post(post).build();
    commentRepository.save(comment);
    String commentId = comment.getId().toString();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "/api/v1/comment/" + postId + "/0", HttpMethod.GET, entity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains(commentId);
    assertThat(response.getBody()).contains(user.getUsername());
    commentRepository.delete(comment);
  }
}
