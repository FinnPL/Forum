package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired private PostRepository postRepository;

  /**
   * addPost to Database
   *
   * @param post Post to add
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public void addPost(Post post) {
    postRepository.save(post);
  }

  /**
   * get All Posts from Database
   *
   * @return List of Posts
   * @see Post
   * @see PostRepository
   * @deprecated Debugging only
   * @since 1.0
   */
  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  /**
   * get Post By ID from Database
   *
   * @param id UUID of Post
   * @return Post
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public Optional<Post> getPostById(UUID id) {
    return postRepository.findById(id);
  }

  /**
   * get Posts By Title Containing String from Database
   *
   * @param title String to search for in Title
   * @return List of Posts
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public List<Post> getAllByTitleContaining(String title) {
    return postRepository.findAllByTitleContaining(title);
  }

  /**
   * Delete Post with ID from Database
   *
   * @param id UUID of Post
   * @return Optional Post that was deleted
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public Optional<Post> deletePost(UUID id) {
    Optional<Post> post = postRepository.findById(id);
    postRepository.deleteById(id);
    return post;
  }

  /**
   * Replace Post with ID from Database
   *
   * @param id UUID of Post
   * @param post Post to replace
   * @return Optional Post that was replaced
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public Optional<Post> updatePost(UUID id, Post post) {
    Optional<Post> post1 = postRepository.findById(id);
    post1.get().setTitle(post.getTitle());
    post1.get().setContent(post.getContent());
    postRepository.save(post1.get());
    return post1;
  }

  /**
   * get Posts by User from Database
   *
   * @param user User to search for
   * @return List of Posts
   * @see Post
   * @see PostRepository
   * @see User
   * @since 1.0
   */
  public List<Post> getAllByUser(User user) {
    return postRepository.findAllByUser(user);
  }

  /**
   * Find 20 posts by Title or Content containing a String
   *
   * @param search String to search for
   * @return List of Posts
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public List<Post> find20ByTitleOrContentContaining(String search) {
    return postRepository
        .findTop20ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByDateDesc(
            search, search);
  }

  /**
   * Find newest 20 Posts after a given time from Database
   *
   * @param date Date to search for
   * @return List of Posts
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public List<Post> find20ByDateAfterOrderByDateDesc(Timestamp date) {
    return postRepository.findTop20ByDateAfterOrderByDateDesc(date);
  }

  /**
   * Find newest 20 Posts from Database
   *
   * @return List of Posts
   * @see Post
   * @see PostRepository
   * @since 1.0
   */
  public List<Post> find20ByOrderByDateDesc() {
    return postRepository.findTop20ByOrderByDateDesc();
  }
}
