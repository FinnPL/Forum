package de.ghse.forum.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@NoArgsConstructor
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
    public Post(UUID id, String title, String content, User user, Collection<Comment> comment, Timestamp date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.comment = comment;
        date = new Timestamp(System.currentTimeMillis());
    }
}
