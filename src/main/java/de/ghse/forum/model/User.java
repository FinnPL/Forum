package de.ghse.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    //Posts of the user
    @ManyToMany(fetch = javax.persistence.FetchType.EAGER)
    private Collection<Post> posts = new ArrayList<>();
}
