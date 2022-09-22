package de.ghse.forum.repository;

import de.ghse.forum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional <Post> getPostById(UUID id);
    List<Post> findPostsByAuthor (String author);
    List <Post> findPostsByTitle(String title);
}
