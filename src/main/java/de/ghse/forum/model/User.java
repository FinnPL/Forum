package de.ghse.forum.model;

import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
  public User() {}

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Collection<Post> posts;

  private String username;
}
