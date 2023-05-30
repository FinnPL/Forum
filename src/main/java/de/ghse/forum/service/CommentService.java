package de.ghse.forum.service;

import de.ghse.forum.model.Comment;
import de.ghse.forum.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for comments.
 *
 * @apiNote This service is mapped to the database table "comment".
 * @see Comment Comment
 * @see de.ghse.forum.repository.CommentRepository CommentRepository
 */
@Service
@RequiredArgsConstructor
public class CommentService {
  @Value("${page.size}")
  private int PAGE_SIZE;

  private final CommentRepository commentRepository;

  /**
   * Add a comment to the database.
   *
   * @param comment The comment to add.
   */
  public void addComment(Comment comment) {
    commentRepository.save(comment);
  }


  /**
   * Delete a comment from the database.
   *
   * @param id The id of the comment to delete.
   */

  public void deleteComment(UUID id) {
    commentRepository.deleteById(id);
  }


  /**
   * Update a comment in the database.
   *
   * @param id The id of the comment to update.
   * @param comment The comment to update.
   */

  public void updateComment(UUID id, Comment comment) {
    Optional<Comment> comment1 = commentRepository.findById(id);
    if (comment1.isEmpty()) return;
    comment1.get().setContent(comment.getContent());
    commentRepository.save(comment1.get());
  }


  /**
   * Find a comment by its id.
   *
   * @param id The id of the comment to find.
   * @return The comment.
   */

  public Optional<Comment> getCommentById(UUID id) {
    return commentRepository.findById(id);
  }



  /**
   * Find all comments for a post.
   *
   * @param post The post to find the comments for.
   * @param page The page to get.
   * @return A list of comments.
   */
  public List<Comment> findCommentByPostByPage(UUID post, int page) {
    return commentRepository.findCommentByPostByPage(post, PageRequest.of(page, PAGE_SIZE));
  }
}
