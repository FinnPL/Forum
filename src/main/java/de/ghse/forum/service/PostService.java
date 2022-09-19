package de.ghse.forum.service;

import de.ghse.forum.dao.PostDao;
import de.ghse.forum.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final PostDao postDao;

    @Autowired
    public PostService(@Qualifier("fakeDao") PostDao postDao) {
        this.postDao = postDao;
    }

    public int addPost(Post post){
        return postDao.insertPost(post);
    }
    public List<Post> getAllPosts(){
        return postDao.selectAllPosts();
    }

    public Optional<Post> getPostById(UUID id){
        return postDao.selectPostById(id);
    }

    public int deletePost(UUID id){
        return postDao.deletePostById(id);
    }

    public int updatePost(UUID id, Post post){
        return postDao.updatePostById(id, post);
    }

}
