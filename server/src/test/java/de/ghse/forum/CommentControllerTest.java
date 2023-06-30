package de.ghse.forum;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
  private User user;
  private Post post;

  @BeforeEach
  void setUp() {
    user = User.builder().username("Kiira").password("vavGPcuEwPZkyst5s").role(Role.USER).build();
    userRepository.save(user);

    post =
        Post.builder()
            .title("figured")
            .content(
                "Terms guides invited main hand gardening tail, claire significant why engineering"
                    + " handled mounted topics, methods selecting want frozen calendar terrorist"
                    + " distinction, bar fought scout workout.")
            .user(user)
            .build();
    postRepository.save(post);
  }

  @AfterEach
  void disassemble() {
    postRepository.delete(post);
    userRepository.delete(user);
  }

  @Test
  void addComment() {
    String token = jwtService.generateToken(user);
    String postId = post.getId().toString();

    CommentRequest commentRequest =
        CommentRequest.builder()
            .content("Were updated engineers acrobat variable swing fund, weblog. ")
            .post_id(postId)
            .build();

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
    String token = jwtService.generateToken(user);
    String postId = post.getId().toString();

    Comment comment =
        Comment.builder()
            .content(
                "Automatic river towards ascii qty washing humanities, wagon objectives"
                    + " championships go impact temple worry, type cet gadgets.")
            .user(user)
            .post(post)
            .build();
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

  @Test
  void deleteComment() {
    String token = jwtService.generateToken(user);

    Comment comment =
        Comment.builder()
            .content(
                "Automatic river towards ascii qty washing humanities, wagon objectives"
                    + " championships go impact temple worry, type cet gadgets.")
            .user(user)
            .post(post)
            .build();
    commentRepository.save(comment);
    String commentId = comment.getId().toString();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "/api/v1/comment/" + commentId, HttpMethod.DELETE, entity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(commentRepository.findById(UUID.fromString(commentId))).isEmpty();
  }

  @Test
  void updateComment() {
    String token = jwtService.generateToken(user);
    String postId = post.getId().toString();

    Comment comment =
        Comment.builder()
            .content(
                "Automatic river towards ascii qty washing humanities, wagon objectives"
                    + " championships go impact temple worry, type cet gadgets.")
            .user(user)
            .post(post)
            .build();
    commentRepository.save(comment);
    String commentId = comment.getId().toString();

    CommentRequest commentRequest =
        CommentRequest.builder()
            .content("Were updated engineers acrobat variable swing fund, weblog. ")
            .post_id(postId)
            .build();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "/api/v1/comment/" + commentId,
            HttpMethod.PUT,
            new HttpEntity<>(commentRequest, headers),
            String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(commentRepository.findById(UUID.fromString(commentId)).get().getContent())
        .isEqualTo(commentRequest.getContent());
    commentRepository.delete(comment);
  }
}
