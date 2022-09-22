package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import de.ghse.forum.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service @Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addPost(Post post){
        return postRepository.save(post);
    }
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public void deletePost(UUID id){
        System.out.println("Delete Post with ID: " + id);
        postRepository.deleteById(id);
    }

    public void updatePost(UUID id, Post post){
        postRepository.deleteById(id);
        postRepository.save(post);
    }

    @Override
    public Optional<Post> getPostById(UUID id) {
        return postRepository.getPostById(id);
    }

    @Override
    public List<Post> findPostsByTitle(String title) {
        return postRepository.findPostsByTitle(title);
    }

    @Override
    public List<Post> findPostsByAuthor(String author) {
        return postRepository.findPostsByAuthor(author);
    }

}
