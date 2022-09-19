package de.ghse.forum.dao;

import de.ghse.forum.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostDao {
    int insertPost(UUID id, Post post);
    default int insertPost(Post post){
        UUID id = UUID.randomUUID();
        return insertPost(id, post);
    }
    List<Post> selectAllPosts();

    Optional<Post> selectPostById(UUID id);

    int deletePostById(UUID id);

    int updatePostById(UUID id, Post post);

}
