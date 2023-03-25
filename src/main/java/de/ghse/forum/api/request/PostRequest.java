package de.ghse.forum.api.request;

import lombok.Builder;
import lombok.Data;
import java.security.Principal;

/**
 * JSON model used for creating a new Post.
 * @apiNote JSON body of the POST /api/v1/post endpoint.
 * @see de.ghse.forum.api.PostController#addPost(PostRequest, Principal) PostController
 */
@Data
@Builder
public class PostRequest {
  private String title;
  private String content;
}
