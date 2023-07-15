package de.ghse.forum.api.request;

import java.security.Principal;

import jakarta.validation.constraints.NotBlank;
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
  @NotBlank private String title;
  @NotBlank private String content;
}
