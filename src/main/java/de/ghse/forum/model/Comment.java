package de.ghse.forum.model;

import java.sql.Timestamp;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
