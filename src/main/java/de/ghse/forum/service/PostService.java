package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  @Autowired private PostRepository postRepository;

  public void addPost(Post post) {
    postRepository.save(post);
  }

  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  public Optional<Post> getPostById(UUID id) {
    return postRepository.findById(id);
  }

  public List<Post> getAllByTitleContaining(String title) {
    return postRepository.findAllByTitleContaining(title);
  }

  public Optional<Post> deletePost(UUID id) {
    Optional<Post> post = postRepository.findById(id);
    postRepository.deleteById(id);
    return post;
  }

  public Optional<Post> updatePost(UUID id, Post post) {
    Optional<Post> post1 = postRepository.findById(id);
    post1.get().setTitle(post.getTitle());
    post1.get().setContent(post.getContent());
    postRepository.save(post1.get());
    return post1;
  }

  public List<Post> getAllByUser(User user) {
    return postRepository.findAllByUser(user);
  }
}
