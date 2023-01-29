package de.ghse.forum.api.request;

import lombok.Data;

@Data
public class PostRequest {
  private String title;
  private String content;
}
