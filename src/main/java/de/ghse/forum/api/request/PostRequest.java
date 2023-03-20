package de.ghse.forum.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostRequest {
  private String title;
  private String content;
}
