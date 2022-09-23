package de.ghse.forum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity @Data
public class Post {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final UUID id;
    @NotBlank
    private final String title;
    @NotBlank
    private final String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
        private User user;

    @NotBlank
    private final String date;

    public Post(@JsonProperty("id") UUID id,
                @JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("user") User user,
                @JsonProperty("date") String date) {
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
        this.date = null;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public  User getUser() {
        return user;
    }

    public @NotBlank String getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

}
