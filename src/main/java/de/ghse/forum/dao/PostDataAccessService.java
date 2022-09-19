package de.ghse.forum.dao;

import de.ghse.forum.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PostDataAccessService implements PostDao {

    @Override
    public int insertPost(UUID id, Post post) {
        return 0;
    }

    @Override
    public List<Post> selectAllPosts() {
        return List.of(new Post(UUID.randomUUID(), "From Postgres DB", "Content", "Author", "Date"));
    }

    @Override
    public Optional<Post> selectPostById(UUID id) {
        return Optional.empty();
    }

    @Override
    public int deletePostById(UUID id) {
        return 0;
    }

    @Override
    public int updatePostById(UUID id, Post post) {
        return 0;
    }
}
