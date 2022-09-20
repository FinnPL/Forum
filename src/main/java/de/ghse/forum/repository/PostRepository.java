package de.ghse.forum.repository;

import de.ghse.forum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Post findPostById(UUID id);
    Post findPostByTitle(String title);

    List<Post> findPostsByAuthor (String author);
}
