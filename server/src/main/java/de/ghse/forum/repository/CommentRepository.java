package de.ghse.forum.repository;

import de.ghse.forum.model.Comment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for comments.
 *
 * @apiNote This repository is mapped to the database table "comment".
 * @see Comment Comment
 * @see de.ghse.forum.service.CommentService CommentService
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
  @Query("SELECT c FROM Comment c WHERE c.post.id = ?1 ORDER BY c.date DESC")
  List<Comment> findCommentByPostByPage(UUID post, Pageable pageable);
}
