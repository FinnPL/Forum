package de.ghse.forum.api.request;

import de.ghse.forum.model.Comment;
import de.ghse.forum.service.CommentService;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import lombok.Data;

import java.security.Principal;
import java.util.UUID;

@Data
public class CommentRequest {
  private String title;
  private String content;
  private String post_id;
}
