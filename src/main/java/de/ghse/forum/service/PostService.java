package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 *
 * <pre>
 * Post Service Class
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@Service
public class PostService {

  private final int PAGE_SIZE = 3;
  @Autowired private PostRepository postRepository;


  public void addPost(Post post) {
    postRepository.save(post);
  }

  public Optional<Post> getPostById(UUID id) {
    return postRepository.findById(id);
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

  public List<Post> getPostsByUserByPage(User user,int page) {
    return postRepository.findPostsByUserByPage(user,PageRequest.of(page, PAGE_SIZE));
  }

  public List<Post> getSearchPage(String query, int page) {
    return postRepository.search(query, PageRequest.of(page, PAGE_SIZE));
  }

  public List<Post> getNewestByPage(int page) {
    return postRepository.getNewestPostByPage(PageRequest.of(page, PAGE_SIZE));
  }
}
