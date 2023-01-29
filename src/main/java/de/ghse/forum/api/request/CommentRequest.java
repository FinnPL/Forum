package de.ghse.forum.api.request;

import lombok.Data;

@Data
public class CommentRequest {
  private String title;
  private String content;
  private String post_id;
}
