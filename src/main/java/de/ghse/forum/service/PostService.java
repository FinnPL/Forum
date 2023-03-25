package de.ghse.forum.service;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service for posts.
 *
 * @apiNote This service is mapped to the database table "post".
 * @see Post Post
 * @see de.ghse.forum.repository.PostRepository PostRepository
 */
@Service
@RequiredArgsConstructor
public class PostService {

  @Value("${page.size}")
  private int PAGE_SIZE;

  private final PostRepository postRepository;

  /**
   * Add a post to the database.
   * @param post The post to add.
   */
  public void addPost(Post post) {
    postRepository.save(post);
  }

    /**
     * Find a post by its id.
     * @param id The id of the post.
     * @return The post.
     */
  public Optional<Post> getPostById(UUID id) {
    return postRepository.findById(id);
  }

    /**
     * Delete a post from the database.
     * @param id The id of the post to delete.
     */
  public void deletePost(UUID id) {
    postRepository.deleteById(id);
  }

    /**
     * Update a post in the database.
     * @param id The id of the post to update.
     * @param post The post to update.
     */
  public void updatePost(UUID id, Post post) {
    Optional<Post> post1 = postRepository.findById(id);
    if (post1.isEmpty()) return;
    post1.get().setTitle(post.getTitle());
    post1.get().setContent(post.getContent());
    postRepository.save(post1.get());
  }

  /**
   * Find posts for a user.
   * @param user The user to find the posts for.
   * @param page The page to get.
   * @return A list of posts.
   */
  public List<Post> getPostsByUserByPage(User user, int page) {
    return postRepository.findPostsByUserByPage(user, PageRequest.of(page, PAGE_SIZE));
  }

    /**
     * Search for posts.
     * @param query The query to search for.
     * @param page The page to get.
     * @return A list of posts.
     */
  public List<Post> getSearchPage(String query, int page) {
    return postRepository.search(query, PageRequest.of(page, PAGE_SIZE));
  }

  /**
   * Get the newest posts.
   * @param page The page to get.
   * @return A list of posts.
   */
  public List<Post> getNewestByPage(int page) {
    return postRepository.getNewestPostByPage(PageRequest.of(page, PAGE_SIZE));
  }
}
