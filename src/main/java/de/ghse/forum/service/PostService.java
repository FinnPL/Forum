package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface PostService {
    Optional<Post> getPostById(UUID id);
    List<Post> findPostsByAuthor(String author);
    List<Post> findPostsByTitle(String title);
}
