package de.ghse.forum.api.response;

import de.ghse.forum.model.Comment;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CommentResponse {
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
