package de.ghse.forum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Post {
    private final UUID id;
    @NotNull
    private final String title;
    @NotNull
    private final String content;
    @NotNull
    private final String author;
    @NotNull
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

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getContent() {
        return content;
    }

    public @NotNull String getAuthor() {
        return author;
    }

    public @NotNull String getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

}
