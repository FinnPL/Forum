package de.ghse.forum.api.request;

import java.security.Principal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model used for creating a new Comment.
 *
 * @apiNote JSON body of the POST /api/v1/comment/add endpoint.
 * @see de.ghse.forum.api.CommentController#addComment(CommentRequest, Principal) CommentController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
  @NotBlank private String content;
  private String post_id;
}
