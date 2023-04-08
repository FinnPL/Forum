package de.ghse.forum.api.response;

import de.ghse.forum.model.Comment;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model returned after fetching a Comment.
 *
 * @see de.ghse.forum.api.CommentController CommentController
 */
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

  /**
   * Converts a List of Comment objects to a List of CommentResponse objects.
   *
   * @param comments List of Comment objects.
   * @return List of CommentResponse objects.
   */
  public List<CommentResponse> convert(List<Comment> comments) {
    List<CommentResponse> commentResponses = new ArrayList<>();
    for (Comment comment : comments) {
      commentResponses.add(this.convert(comment));
    }
    return commentResponses;
  }

  /**
   * Converts a Comment object to a CommentResponse object.
   *
   * @param comment Comment object.
   * @return CommentResponse object.
   */
  public CommentResponse convert(Comment comment) {
    return CommentResponse.builder()
        .id(comment.getId().toString())
        .content(comment.getContent())
        .user_id(comment.getUser().getId().toString())
        .user_name(comment.getUser().getUsername())
        .date(comment.getDate().toString())
        .build();
  }
}
