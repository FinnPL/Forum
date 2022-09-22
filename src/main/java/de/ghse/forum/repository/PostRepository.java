package de.ghse.forum.repository;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Post findByTitle(String title);
}
