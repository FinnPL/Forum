package de.ghse.forum.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 *
 * <pre>
 * Post Model in Database
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
public class Post {

  @Id
  @Column(name = "id", columnDefinition = "BINARY(16)")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotBlank private String title;

  @NotBlank private String content;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private Collection<Comment> comment;

  private java.sql.Timestamp date = new Timestamp(System.currentTimeMillis());

  /**
   *
   *
   * <pre>
   * No Args Constructor for Post
   * </pre>
   *
   * @since 1.0
   */
  public Post() {}
}
