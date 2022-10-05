package de.ghse.forum.model;

import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

/**
 *
 *
 * <pre>
 * User Model in Database
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Collection<Post> posts;

  private String username;
}
