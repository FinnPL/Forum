package de.ghse.forum.service;

import de.ghse.forum.model.Comment;
import de.ghse.forum.repository.CommentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
   * @param comment The comment to add.
   */
  public void addComment(Comment comment) {
    commentRepository.save(comment);
  }

  /**
   * Find all comments for a post.
   * @param post The post to find the comments for.
   * @param page The page to get.
   * @return A list of comments.
   */
  public List<Comment> findCommentByPostByPage(UUID post, int page) {
    return commentRepository.findCommentByPostByPage(post, PageRequest.of(page, PAGE_SIZE));
  }
}
