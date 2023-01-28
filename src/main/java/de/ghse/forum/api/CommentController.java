package de.ghse.forum.api;

import de.ghse.forum.api.request.CommentRequest;
import de.ghse.forum.api.response.CommentResponse;
import de.ghse.forum.model.Comment;
import de.ghse.forum.service.CommentService;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;
  private final UserService userService;

  private final PostService postService;

  @PostMapping(path = "/add/")
  public void addComment(@RequestBody CommentRequest commentRequest) {
    Comment comment = new Comment();
    comment.setTitle(commentRequest.getTitle());
    comment.setContent(commentRequest.getText());
    comment.setUser(
        userService.findUserById(UUID.fromString(commentRequest.getUser_id())).orElseThrow());
    comment.setPost(
        postService.getPostById(UUID.fromString(commentRequest.getPost_id())).orElseThrow());
    commentService.addComment(comment);
  }

  @GetMapping(path = "/get/{post_id}/{page}")
  public List<CommentResponse> getCommentsByPostByPage(
      @PathVariable("post_id") UUID post_id, @PathVariable("page") int page) {
    return new CommentResponse().convert(commentService.findCommentByPostByPage(post_id, page));
  }
}
