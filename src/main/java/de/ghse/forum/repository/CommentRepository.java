package de.ghse.forum.repository;

import de.ghse.forum.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
 List<Comment> findTop20OrderByDateDesc();
 List<Comment> findTop20ByDateAfterOrderByDateDesc(Timestamp date);
}
