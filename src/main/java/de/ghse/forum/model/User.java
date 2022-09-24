package de.ghse.forum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;



import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity @Data
public class User {
    public User() {}
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private  UUID id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Post> posts;

    private  String username;
}
