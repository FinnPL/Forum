package de.ghse.forum.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
  private String content;
  private String post_id;
}
