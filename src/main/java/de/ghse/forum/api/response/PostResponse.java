package de.ghse.forum.api.response;

import de.ghse.forum.model.Post;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model returned after fetching a Post.
 * @see de.ghse.forum.api.PostController PostController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  private String id;
  private String title;
  private String content;
  private String user_id;
  private String user_name;
  private String date;
  private boolean edited;

    /**
     * Converts a List of Post objects to a List of PostResponse objects.
     * @param posts List of Post objects.
     * @return List of PostResponse objects.
     */
  public List<PostResponse> convert(List<Post> posts) {
    List<PostResponse> postResponses = new ArrayList<>();
    for (Post post : posts) {
      postResponses.add(this.convert(post));
    }
    return postResponses;
  }

    /**
     * Converts a Post object to a PostResponse object.
     * @param post Post object.
     * @return PostResponse object.
     */
  public PostResponse convert(Post post) {
    return PostResponse.builder()
        .id(post.getId().toString())
        .title(post.getTitle())
        .content(post.getContent())
        .user_id(post.getUser().getId().toString())
        .user_name(post.getUser().getUsername())
        .date(post.getDate().toString())
        .edited(post.isEdited())
        .build();
  }
}
