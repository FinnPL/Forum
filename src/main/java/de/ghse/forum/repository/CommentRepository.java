package de.ghse.forum.repository;

import de.ghse.forum.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
 @Query("SELECT c FROM Comment c WHERE c.post.id = ?1 ORDER BY c.date DESC")
  List<Comment> findCommentByPostByPage(UUID post, Pageable pageable);
}
