package de.ghse.forum.model;

import com.fasterxml.jackson.annotation.JsonProperty;


import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Post {
    private final UUID id;
    @NotBlank
    private final String title;
    @NotBlank
    private final String content;
    @NotBlank
    private final String author;
    @NotBlank
    private final String date;

    public Post(@JsonProperty("id") UUID id,
                @JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("author") String author,
                @JsonProperty("date") String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public @NotBlank String getAuthor() {
        return author;
    }

    public @NotBlank String getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

}
