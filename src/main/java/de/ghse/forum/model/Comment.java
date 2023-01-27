package de.ghse.forum.model;

import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Comment {
  @Id
  @Column(name = "id", columnDefinition = "BINARY(16)")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotBlank private String title;
  @NotBlank private String content;

  @ManyToOne private User user;

  @ManyToOne private Post post;

  private java.sql.Timestamp date = new Timestamp(System.currentTimeMillis());
}
