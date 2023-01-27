package de.ghse.forum.api;

import de.ghse.forum.model.Comment;
import de.ghse.forum.service.CommentService;
import de.ghse.forum.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/comment")
@RestController
public class CommentController {
  private final CommentService commentService;
  private final UserService userService;

  @Autowired
  public CommentController(CommentService commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
  }

  @PostMapping(path = "/add")
  public void addComment(@RequestBody CommentRequest commentRequest) {
    Comment comment = new Comment();
    comment.setUser(
        userService.findUserById(UUID.fromString(commentRequest.getUser_id())).orElseThrow());
    comment.setTitle(commentRequest.getTitle());
    comment.setContent(commentRequest.getText());
    commentService.addComment(comment);
  }

  @GetMapping(path = "/get/{post_id}/{page}")
  public List<CommentResponse> getCommentsByPostByPage(
      @PathVariable("post_id") UUID post_id, @PathVariable("page") int page) {
    return new CommentResponse().convert(commentService.findCommentByPostByPage(post_id, page));
  }

  @Data
  public static class CommentRequest {
    private String title;
    private String text;
    private String user_id;
    private String post_id;
  }

  @Data
  public static class CommentResponse {
    private String id;
    private String title;
    private String content;
    private String user_id;
    private String user_name;
    private String date;

    public List<CommentResponse> convert(List<Comment> comments) {
      List<CommentResponse> commentResponses = new ArrayList<>();
      for (Comment comment : comments) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(String.valueOf(comment.getId()));
        commentResponse.setTitle(comment.getTitle());
        commentResponse.setContent(comment.getContent());
        commentResponse.setUser_id(String.valueOf(comment.getUser().getId()));
        commentResponse.setUser_name(comment.getUser().getUsername());
        commentResponse.setDate(String.valueOf(comment.getDate()));
        commentResponses.add(commentResponse);
      }
      return commentResponses;
    }

    public CommentResponse convert(Comment comment) {
      CommentResponse commentResponse = new CommentResponse();
      commentResponse.setId(String.valueOf(comment.getId()));
      commentResponse.setTitle(comment.getTitle());
      commentResponse.setContent(comment.getContent());
      commentResponse.setUser_id(String.valueOf(comment.getUser().getId()));
      commentResponse.setUser_name(comment.getUser().getUsername());
      commentResponse.setDate(String.valueOf(comment.getDate()));
      return commentResponse;
    }
  }
}
