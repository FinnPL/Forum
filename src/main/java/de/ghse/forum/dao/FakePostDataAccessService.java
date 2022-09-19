package de.ghse.forum.dao;

import de.ghse.forum.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePostDataAccessService implements PostDao {
    private static List<Post> DB = new ArrayList<>();

    @Override
    public int insertPost(UUID id, Post post) {
        DB.add(new Post(id, post.getTitle(), post.getContent(), post.getAuthor(), post.getDate()));
        return 1;
    }

    @Override
    public List<Post> selectAllPosts() {
        return DB;
    }

    @Override
    public Optional<Post> selectPostById(UUID id) {
        return DB.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deletePostById(UUID id) {
        Optional<Post> postMaybe = selectPostById(id);
        if (postMaybe.isEmpty()) {
            return 0;
        }
        DB.remove(postMaybe.get());
        return 1;
    }

    @Override
    public int updatePostById(UUID id, Post update) {
        return selectPostById(id)
                .map(p -> {
                    int indexOfPostToUpdate = DB.indexOf(p);
                    if (indexOfPostToUpdate >= 0) {
                        DB.set(indexOfPostToUpdate, new Post(id, update.getTitle(), update.getContent(), update.getAuthor(), update.getDate()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }

}
