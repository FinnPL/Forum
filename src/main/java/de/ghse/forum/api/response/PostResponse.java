package de.ghse.forum.api.response;

import de.ghse.forum.model.Post;
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
public class PostResponse {
  private String id;
  private String title;
  private String content;
  private String user_id;
  private String user_name;
  private String date;
  private boolean edited;

  public List<PostResponse> convert(List<Post> posts) {
    List<PostResponse> postResponses = new ArrayList<>();
    for (Post post : posts) {
      postResponses.add(this.convert(post));
    }
    return postResponses;
  }

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
