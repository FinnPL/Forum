package de.ghse.forum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity Model for comments.
 *
 * @apiNote This model is mapped to the database table "comment".
 * @see Post Post
 * @see User User
 * @see de.ghse.forum.service.CommentService CommentService
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
  @Id
  @Column(name = "id", columnDefinition = "BINARY(16)")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotBlank private String content;

  @ManyToOne private User user;

  @ManyToOne private Post post;

  @Builder.Default private java.sql.Timestamp date = new Timestamp(System.currentTimeMillis());

  @Builder.Default private boolean edited = false;
}
