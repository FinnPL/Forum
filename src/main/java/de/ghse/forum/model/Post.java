package de.ghse.forum.model;


import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.UUID;

/**
    * <pre>
    *Post Model in Database
    * </pre>
    * @version 1.0
    * @since 1.0
    */
@Entity @Data
public class Post {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  UUID id;

    @NotBlank
    private  String title;

    @NotBlank
    private  String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Timestamp date;

    /**
     * <pre>
     *No Args Constructor for Post
     * </pre>
     * @since 1.0
     */
    public Post() {}

}
