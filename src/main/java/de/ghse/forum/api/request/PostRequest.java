package de.ghse.forum.api.request;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model used for creating a new Post.
 *
 * @apiNote JSON body of the POST /api/v1/post endpoint.
 * @see de.ghse.forum.api.PostController#addPost(PostRequest, Principal) PostController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
  private String title;
  private String content;
}
