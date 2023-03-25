package de.ghse.forum.api.request;

import lombok.Builder;
import lombok.Data;

import java.security.Principal;

/**
 * JSON model used for creating a new Comment.
 * @apiNote JSON body of the POST /api/v1/comment/add endpoint.
 * @see de.ghse.forum.api.CommentController#addComment(CommentRequest, Principal) CommentController
 */
@Data
@Builder
public class CommentRequest {
  private String content;
  private String post_id;
}
