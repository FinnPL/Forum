package de.ghse.forum.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Post {
  @Id
  @Column(name = "id", columnDefinition = "BINARY(16)")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final UUID id;

  @NotBlank private String title;
  @NotBlank private String content;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private Timestamp date;

  public Post(UUID id, String title, String content, User user, Timestamp date) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.user = user;
    this.date = date;
  }

  public Post() {
    this.id = null;
    this.title = null;
    this.content = null;
    this.date = new Timestamp(new Date().getTime());
  }

  public @NotBlank String getTitle() {
    return title;
  }

  public @NotBlank String getContent() {
    return content;
  }

  public User getUser() {
    return user;
  }

  public Timestamp getDate() {
    return date;
  }

  public UUID getId() {
    return id;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
