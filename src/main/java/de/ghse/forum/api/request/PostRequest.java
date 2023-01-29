package de.ghse.forum.api.request;

import de.ghse.forum.api.PostController;
import de.ghse.forum.model.Post;
import lombok.Data;


@Data
public class PostRequest {
  private String title;
  private String content;

}
