package de.ghse.forum.api;

import de.ghse.forum.api.request.CommentRequest;
import de.ghse.forum.api.response.CommentResponse;
import de.ghse.forum.model.Comment;
import de.ghse.forum.service.CommentService;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for comment related endpoints.
 *
 * @apiNote This controller is accessible under /api/v1/comment.
 * @see CommentService
 * @see Comment
 */
@RequestMapping("api/v1/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;
  private final UserService userService;
  private final PostService postService;

  final Logger logger = LoggerFactory.getLogger(CommentController.class);

  /**
   * REST endpoint for adding a comment.
   *
   * @apiNote This endpoint is accessible under /api/v1/comment/add.
   * @param commentRequest the JSON body of the request
   * @param principal the principal of the user (Spring internal)
   * @see CommentRequest
   * @see Comment
   */
  @PostMapping
  public ResponseEntity<CommentResponse> addComment(
      @RequestBody CommentRequest commentRequest, Principal principal) {
    logger.info("Adding comment to post with id: " + commentRequest.getPost_id());
    try {
      Comment comment =
          Comment.builder()
              .content(commentRequest.getContent())
              .user(userService.findbyUsername(principal.getName()).orElseThrow())
              .post(
                  postService
                      .getPostById(UUID.fromString(commentRequest.getPost_id()))
                      .orElseThrow())
              .build();
      commentService.addComment(comment);
      return ResponseEntity.ok(new CommentResponse().convert(comment));
    } catch (Exception e) {
      logger.error("Error adding comment" + e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * REST endpoint for getting comments of a post.
   *
   * @apiNote This endpoint is accessible under /api/v1/comment/get/{post_id}/{page}.
   * @param post_id the UUID of the post
   * @param page the page number
   * @return a list of CommentResponse
   * @see CommentResponse
   * @see Comment
   */
  @GetMapping(path = "/{post_id}/{page}")
  public List<CommentResponse> getCommentsByPostByPage(
      @PathVariable("post_id") UUID post_id, @PathVariable("page") int page) {
    logger.info("Getting comments from post with id: " + post_id);
    return new CommentResponse().convert(commentService.findCommentByPostByPage(post_id, page));
  }
}
