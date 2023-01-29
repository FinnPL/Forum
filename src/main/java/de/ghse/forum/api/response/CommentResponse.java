package de.ghse.forum.api.response;

import de.ghse.forum.model.Comment;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  private String id;
  private String content;
  private String user_id;
  private String user_name;
  private String date;

  public List<CommentResponse> convert(List<Comment> comments) {
    List<CommentResponse> commentResponses = new ArrayList<>();
    for (Comment comment : comments) {
      commentResponses.add(
          CommentResponse.builder()
              .id(comment.getId().toString())
              .content(comment.getContent())
              .user_id(comment.getUser().getId().toString())
              .user_name(comment.getUser().getUsername())
              .date(comment.getDate().toString())
              .build());
    }
    return commentResponses;
  }
}
